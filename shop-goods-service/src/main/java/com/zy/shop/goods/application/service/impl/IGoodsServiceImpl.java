package com.zy.shop.goods.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.zy.shop.common.dto.mq.MQMessageEntity;
import com.zy.shop.common.exception.ShopBizException;
import com.zy.shop.common.util.RedisLock;
import com.zy.shop.goods.application.mapper.ShopGoodsMapper;
import com.zy.shop.goods.application.mapper.ShopMqConsumerLogMapper;
import com.zy.shop.goods.application.service.IGoodsService;
import com.zy.shop.pojo.ShopGoods;
import com.zy.shop.pojo.ShopMQConsumerLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import static com.zy.shop.common.enums.ShopGoodsStatusEnum.*;
import static com.zy.shop.common.enums.ShopMqMsgHandleStatusEnum.SHOP_MQ_MSG_STATUS_SUCCESS;

/**
 * @author: jogin
 * @date: 2020/12/5 18:59
 */

@Slf4j
@Service
public class IGoodsServiceImpl implements IGoodsService {

    @Autowired
    private ShopGoodsMapper goodsMapper;
    @Autowired
    private ShopMqConsumerLogMapper mqConsumerLogMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    @Qualifier("poolTaskExecutor")
    private ThreadPoolTaskExecutor poolTaskExecutor;

    /**
     * 查询商品
     *
     * @param goodsId 商品Id
     * @return 商品
     */
    @Override
    public ShopGoods findOneById(Long goodsId) {
        log.info("查询商品：{}", goodsId);
        return goodsMapper.findOneById(goodsId);
    }

    /**
     * 扣减商品库存
     *
     * @param shopGoods 商品库存历史记录
     * @return 是否扣减成功
     */
    @Override
    @Transactional
    public boolean reduceGoodsNumber(ShopGoods shopGoods, Long orderId) throws ShopBizException {

        // 幂等
        Integer state = (Integer) redisTemplate.opsForValue().get(shopGoods.getGoodsId());
        if (state != null && SHOP_GOOD_STATUS_NUMBER_REDUCING.getCode().intValue() == state.intValue()) {
            poolTaskExecutor.execute(() -> {
                int reties = 3;
                boolean result = false;
                try {
                    while (reties > 0 && !result) {
                        result = redisTemplate.opsForValue().setIfAbsent(orderId, SHOP_GOOD_STATUS_NUMBER_REDUCING.getCode());
                        reties--;
                        if (!result) {
                            Thread.sleep(200);
                        }
                    }
                } catch (InterruptedException e) {
                    log.error("商品扣减库存执行幂等线程被中断：{}", e);
                }
            });
        }

        ShopGoods goods = goodsMapper.findOneById(shopGoods.getGoodsId());
        if (goods == null) {
            log.info("查询不到商品：{}", goods.getGoodsId());
            throw new ShopBizException(SHOP_GOODS_STATUS_NOT_FOUND.toString());
        }
        if (goods.getNumber() - shopGoods.getNumber() < 0) {
            log.info("商品库存不足：{}", goods.getNumber());
            throw new ShopBizException(SHOP_GOODS_STATUS_NUMBER_INSUFFICIENT.toString());
        }
        try {
            // 加锁，扣减商品库存
            RedisLock.lock(redisTemplate, String.valueOf(shopGoods.getGoodsId()));
            int num = goods.getNumber() - shopGoods.getNumber();
            goods.setNumber(num);
            goodsMapper.updateGoods(goods);
            // 释放幂等和锁
            redisTemplate.delete(shopGoods.getGoodsId());
            RedisLock.unlockLua(redisTemplate, String.valueOf(goods.getGoodsId()));
            log.info("成功扣减商品：{} 库存：{}", shopGoods.getGoodsId(), num);
        } catch (Exception e) {
            log.error("扣减商品库存失败：{}", e.getMessage(), e);
            throw new ShopBizException("扣减商品库存出错", e);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean rollbackGoodNumber(Map<String, String> msgContentMap) {
        Long goodsId = null;
        ShopMQConsumerLog consumerLog = mqConsumerLogMapper.findOneByMsgKey(msgContentMap.get("msgKey"));
        try {
            if (!canConsumeMessage(consumerLog, msgContentMap)) {
                return false;
            }
            consumeMessage(consumerLog, msgContentMap);
            MQMessageEntity message = JSON.parseObject(msgContentMap.get("msgBody"), MQMessageEntity.class);
            goodsId = message.getGoodId();
            ShopGoods goods = goodsMapper.findOneById(goodsId);
            goods.setNumber(goods.getNumber() + message.getGoodNumber());
            goodsMapper.updateGoods(goods);

            consumerLog.setStatus(SHOP_MQ_MSG_STATUS_SUCCESS.getCode());
            consumerLog.setUpdateTime(new Timestamp(new Date().getTime()));
            mqConsumerLogMapper.updateMqConsumerLog(consumerLog);
            log.info("商品：{} 数量回滚成功", message.getGoodNumber());
        } catch (Exception e) {
            log.warn("回滚商品：{} 数量出现异常：{}", goodsId, e.getMessage());
            invokeException(msgContentMap);
        }
        return false;
    }

    private boolean canConsumeMessage(ShopMQConsumerLog mqConsumerLog, Map<String, String> msgContentMap) {
        if (mqConsumerLog != null) {
            Integer status = mqConsumerLog.getStatus();
            if (SHOP_MQ_MSG_STATUS_SUCCESS.getCode().intValue() == status.intValue()) {
                log.info("消息：{} 已经消费成功", msgContentMap.get("msgId"));
                return false;
            }
            if (SHOP_GOOD_STATUS_NUMBER_REDUCING.getCode().intValue() == status.intValue()) {
                log.info("消息：{} 正在消费中", msgContentMap.get("msgId"));
                return false;
            }
            if (SHOP_GOOD_STATUS_REDUCE_FAIL.getCode().intValue() == status.intValue()) {
                if (mqConsumerLog.getConsumeTime() > 3) {
                    log.info("消息：{} 消息处理超过3次,不能再进行处理", msgContentMap.get("msgId"));
                    return false;
                }
            }
        }
        return true;
    }

    private void consumeMessage(ShopMQConsumerLog consumerLog, Map<String, String> msgContentMap) {
        // mq 消息处理记录落库
        if (consumerLog == null) {
            saveMqConsumerLog(msgContentMap, 0);
            log.info("开始消费消息：{}", msgContentMap.get("msgId"));
        } else {
            consumerLog.setConsumeTime(consumerLog.getConsumeTime() + 1);
            consumerLog.setStatus(SHOP_GOOD_STATUS_NUMBER_REDUCING.getCode());
            mqConsumerLogMapper.saveMqConsumerLog(consumerLog);
            log.info("消息开始第 [{}] 重试消费", consumerLog.getConsumeTime() + 1);
        }
    }

    private void invokeException(Map<String, String> msgContentMap) {
        ShopMQConsumerLog mqConsumerLog = mqConsumerLogMapper.findOneByMsgKey(msgContentMap.get("msgKey"));
        if (mqConsumerLog == null) {
            saveMqConsumerLog(msgContentMap, 1);
        } else {
            mqConsumerLog.setStatus(SHOP_GOOD_STATUS_NUMBER_REDUCING.getCode());
            mqConsumerLog.setConsumeTime(mqConsumerLog.getConsumeTime() + 1);
            mqConsumerLogMapper.updateMqConsumerLog(mqConsumerLog);
        }
        log.info("消息：{} 消费失败，消费次数：{}，等待重试消费", msgContentMap.get("msgId"), mqConsumerLog.getConsumeTime());
        rollbackGoodNumber(msgContentMap);
    }

    private void saveMqConsumerLog(Map<String, String> msgContentMap, int times) {
        ShopMQConsumerLog mqConsumerLog = new ShopMQConsumerLog();
        mqConsumerLog.setMsgKey(msgContentMap.get("msgKey"));
        mqConsumerLog.setGroupName(msgContentMap.get("msgGroup"));
        mqConsumerLog.setMsgTag(msgContentMap.get("msgTag"));
        mqConsumerLog.setMsgBody(msgContentMap.get("msgBody"));
        mqConsumerLog.setConsumeTime(times);
        mqConsumerLog.setMsgId(Long.parseLong(msgContentMap.get("msgId")));
        mqConsumerLog.setStatus(SHOP_GOOD_STATUS_NUMBER_REDUCING.getCode());
        mqConsumerLogMapper.saveMqConsumerLog(mqConsumerLog);
    }
}

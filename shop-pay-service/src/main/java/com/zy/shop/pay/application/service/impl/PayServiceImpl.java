package com.zy.shop.pay.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.zy.shop.common.exception.ShopBizException;
import com.zy.shop.common.util.IDWorker;
import com.zy.shop.pay.application.mapper.ShopMqProducerLogMapper;
import com.zy.shop.pay.application.mapper.ShopPayMapper;
import com.zy.shop.pay.application.service.IPayService;
import com.zy.shop.pojo.ShopMQProducerLog;
import com.zy.shop.pojo.ShopPay;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

import static com.zy.shop.common.enums.ShopMqMsgHandleStatusEnum.SHOP_MQ_MSG_STATUS_SUCCESS;
import static com.zy.shop.common.enums.ShopPaidStatusEnum.*;

/**
 * @author: jogin
 * @date: 2020/12/6 14:06
 */

@Slf4j
@Service
// TODO 幂等
public class PayServiceImpl implements IPayService {

    @Autowired
    private ShopPayMapper shopPayMapper;
    @Autowired
    private ShopMqProducerLogMapper mqProducerLogMapper;
    @Autowired
    @Qualifier("poolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IDWorker idWorker;

    @Value("${rocketmq.producer.group}")
    private String groupName;
    @Value("${mq.topic}")
    private String topic;
    @Value("${mq.pay.tag}")
    private String tag;

    /**
     * 创建支付订单
     *
     * @param shopPay 订单支付实体类
     * @return 是否创建成功
     */
    @Override
    @Transactional
    public boolean createPayment(ShopPay shopPay) throws ShopBizException{
        // 判断支付状态
        if (shopPayMapper.countById(shopPay.getPayId()) > 0) {
            throw new ShopBizException(SHOP_PAY_STATUS_PAID.toString());
        }
        // 设置订单状态未支付 并 保存
        shopPay.setStatus(SHOP_PAY_STATUS_UNPAID.getCode());
        int row = shopPayMapper.saveShopPay(shopPay);
        return row > 0;
    }

    /**
     * 订单支付
     *
     * @param shopPay 订单支付实体类
     * @return 是否支付成功
     */
    @Override
    @Transactional
    public boolean callbackPayment(ShopPay shopPay) throws ShopBizException{
        log.info("支付回调：{}", shopPay);
        // 判断订单是否存在
        if (shopPayMapper.findOneById(shopPay.getPayId()) == null) {
            throw new ShopBizException(SHOP_PAY_STATUS_NOT_FOUND.toString());
        }
        // 判断订单支付状态
        if (!shopPay.getStatus().equals(SHOP_PAY_STATUS_PAID.getCode())) {
            throw new ShopBizException(SHOP_PAY_STATUS_ROLLBACK_FAIL2.toString());
        }
        try {
            // 订单支付持久化
            shopPay.setStatus(SHOP_PAY_STATUS_PAID.getCode());
            shopPayMapper.saveShopPay(shopPay);
            // 发送 mq
            sendMqMessage(shopPay);
            log.info("订单支付成功：{}", shopPay.getPayId());
        } catch (Exception e) {
            throw new ShopBizException("订单支付失败", e);
        }
        return true;
    }

    /**
     * 发送 mq 消息
     *
     * @param shopPay 支付订单实体类
     */
    private void sendMqMessage(ShopPay shopPay) throws ShopBizException{
        ShopMQProducerLog mqProducerLog = new ShopMQProducerLog();
        mqProducerLog.setMsgId(idWorker.nextId());
        mqProducerLog.setGroupName(groupName);
        mqProducerLog.setMsgTopic(topic);
        mqProducerLog.setMsgKey(String.valueOf(shopPay.getPayId()));
        mqProducerLog.setMsgBody(JSON.toJSONString(shopPay));
        mqProducerLog.setCreateTime(new Timestamp(new Date().getTime()));
        mqProducerLogMapper.saveProducerLog(mqProducerLog);
        Message message = new Message(topic, tag, String.valueOf(shopPay.getPayId()), JSON.toJSONString(shopPay).getBytes());
        threadPoolTaskExecutor.submit(() -> {
            try{
                log.info("构造 RocketMq 消息：{}", message);
                boolean result = sendMessage(message);
                if (result) {
                    log.info("RocketMq 发送消息成功：{}", message);
                    mqProducerLog.setStatus(SHOP_MQ_MSG_STATUS_SUCCESS.getCode());
                    mqProducerLogMapper.updateProducerLog(mqProducerLog);
                }
            }catch (Exception e){
                log.error("发送 mq 消息失败：{}",e.getMessage(),e);
            }
        });
    }

    /**
     * 发送 mq 消息
     *
     * @param message mq 消息
     * @return 是否发送成功
     */
    private boolean sendMessage(Message message) throws ShopBizException{
        try {
            SendResult sendResult = rocketMQTemplate.getProducer().send(message);
            if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                return true;
            }
        } catch (Exception e) {
            log.error("发送 RocketMq 失败：{}", e.getMessage(), e);
            throw new ShopBizException("发送 RocketMq 消息失败", e);
        }
        return false;
    }
}

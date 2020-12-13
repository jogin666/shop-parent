package com.zy.shop.conpon.application.mq;

import com.alibaba.fastjson.JSON;
import com.zy.shop.common.dto.mq.MQMessageEntity;
import com.zy.shop.conpon.application.service.ICouponService;
import com.zy.shop.pojo.ShopCoupon;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;

import static com.zy.shop.common.enums.ShopCouponStatusEnum.SHOP_COUPON_UNUSED;

/**
 * @author: jogin
 * @date: 2020/12/5 16:06
 */

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic}", consumerGroup = "${mq.order.consumer.group.name}", messageModel = MessageModel.BROADCASTING)
public class RollBackCouponStatusListener implements RocketMQListener<MessageExt> {

    @Autowired
    private ICouponService ICouponService;

    /**
     * 将优惠卷状态更新为未使用
     *
     * @param messageExt
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            // 1. 解析消息内容
            String body = new String(messageExt.getBody(), "UTF-8");
            MQMessageEntity message = JSON.parseObject(body, MQMessageEntity.class);
            log.info("接收到 MQ 信息：{}", message);
            if (StringUtils.hasLength(String.valueOf(message.getCouponId()))) {
                // 2. 查询优惠卷信息
                ShopCoupon tradeCoupon = ICouponService.findOneById(message.getCouponId());
                if (tradeCoupon != null) {
                    log.info("查询不到优惠卷：{}", message.getCouponId());
                    return;
                }
                // 3. 更改优惠卷状态
                tradeCoupon.setUseTime(null);
                tradeCoupon.setStatus(SHOP_COUPON_UNUSED.getCode());
                ICouponService.updateCouponStatus(tradeCoupon);
                log.info("回滚优惠卷成功：{}", message.getCouponId());
            }
        } catch (UnsupportedEncodingException e) {
            log.error("RocketMQ 消息 UTF-8 编码失败：{}", e.getMessage(), e);
        }
    }
}

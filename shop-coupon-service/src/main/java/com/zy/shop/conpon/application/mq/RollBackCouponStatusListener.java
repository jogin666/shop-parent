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

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.sql.Timestamp;

import static com.zy.shop.common.enums.ShopCouponStatusEnum.SHOP_COUPON_UNUSED;

/**
 * @Author: Jong
 * @Date: 2020/12/5 16:06
 */

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.cancel.topic}", consumerGroup = "${mq.order.cancel.consumer.group}", messageModel = MessageModel.BROADCASTING)
public class RollBackCouponStatusListener implements RocketMQListener<MessageExt> {

    @Autowired
    private ICouponService ICouponService;

    /**
     * 将优惠卷状态更新为未使用
     *
     * @param messageExt rocketmq 支付订单失败消息
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        // 1. 解析消息内容
        String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        MQMessageEntity message = JSON.parseObject(body, MQMessageEntity.class);
        log.info("接收到 MQ 信息：{}", message);
        if (StringUtils.hasLength(String.valueOf(message.getCouponId()))) {
            // 2. 查询优惠卷信息
            ShopCoupon tradeCoupon = ICouponService.findOneById(message.getCouponId());
            if (tradeCoupon == null) {
                log.info("查询不到优惠卷：{}", message.getCouponId());
                return;
            }
            // 3. 更改优惠卷状态
            tradeCoupon.setUseTime(new Timestamp(new Date().getTime()));
            tradeCoupon.setStatus(SHOP_COUPON_UNUSED.getCode());
            ICouponService.updateCouponStatus(tradeCoupon);
            log.info("回滚优惠卷成功：{}", message.getCouponId());
        }
    }
}

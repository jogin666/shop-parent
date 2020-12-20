package com.zy.shop.order.applicaton.mq;

import com.alibaba.fastjson.JSON;
import com.zy.shop.common.enums.ShopPaidStatusEnum;
import com.zy.shop.order.applicaton.service.IOrderService;
import com.zy.shop.pojo.ShopOrder;
import com.zy.shop.pojo.ShopPay;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @Author Jong
 * @Date 2020/12/19 15:01
 * @Version 1.0
 */

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.pay.topic}", consumerGroup = "${mq.pay.consumer.group}", messageModel = MessageModel.BROADCASTING)
public class PaymentMQListener implements RocketMQListener<MessageExt> {

    @Autowired
    private IOrderService orderService;

    @Override
    public void onMessage(MessageExt message) {
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            log.info("rocketmq 接受到消息：{}", body);
            ShopPay ShopPay = JSON.parseObject(body, ShopPay.class);
            //2. 查询订单
            ShopOrder order = new ShopOrder();
            order.setOrderId(ShopPay.getOrderId());
            //3.更新订单状态为取消
            order.setOrderStatus(ShopPaidStatusEnum.SHOP_PAY_STATUS_PAID.getCode());
            orderService.cancelOrder(order);
            log.info("更新订单状态为支付成功：{}", order.getOrderId());
        } catch (Exception e) {
            log.error("更新订单状态为支付失败：{}", e.getMessage(), e);
        }
    }
}

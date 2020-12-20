package com.zy.shop.order.applicaton.mq;

import com.alibaba.fastjson.JSON;
import com.zy.shop.common.dto.mq.MQMessageEntity;
import com.zy.shop.common.enums.ShopOrderStatusEnum;
import com.zy.shop.order.applicaton.service.IOrderService;
import com.zy.shop.pojo.ShopOrder;
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
@RocketMQMessageListener(topic = "${mq.order.cancel.topic}",consumerGroup = "${mq.order.cancel.consumer.group}",messageModel = MessageModel.BROADCASTING )
public class OrderCancelMQListener implements RocketMQListener<MessageExt> {

    @Autowired
    private IOrderService orderService;

    @Override
    public void onMessage(MessageExt message) {
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            log.info("rocketmq 接受到消息：{}", body);
            MQMessageEntity msgEntity = JSON.parseObject(body, MQMessageEntity.class);
            //2. 查询订单
            ShopOrder order = new ShopOrder();
            order.setOrderId(msgEntity.getOrderId());
            //3.更新订单状态为取消
            order.setOrderStatus(ShopOrderStatusEnum.SHOP_ORDER_STATUS_CANCEL.getCode());
            orderService.cancelOrder(order);
            log.info("订单已取消：{}", order.getOrderId());
        } catch (Exception e) {
            log.error("订单取消失败：{}", e.getMessage(), e);
        }
    }
}

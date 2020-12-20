package com.zy.shop.goods.application.mq;

import com.zy.shop.goods.application.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: Jong
 * @Date: 2020/12/6 13:02
 */

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.cancel.topic}", consumerGroup = "${mq.order.cancel.consumer.group}", messageModel = MessageModel.BROADCASTING)
public class RollbackGoodsNumberListener implements RocketMQListener<MessageExt> {

    @Autowired
    private IGoodsService goodsService;
    @Value("${mq.order.consumer.group.name}")
    private String groupName;

    /**
     * 回滚商品库存
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        // 1.解析消息内容
        log.info("接收到 RocketMQ 消息：{}", messageExt);
        Map<String, String> msgContentMap = new HashMap<>();
        msgContentMap.put("msgId", messageExt.getMsgId());
        msgContentMap.put("msgTag", messageExt.getTags());
        msgContentMap.put("msgKey", messageExt.getKeys());
        msgContentMap.put("msgGroup", groupName);
        msgContentMap.put("msgBoy", new String(messageExt.getBody(), StandardCharsets.UTF_8));
        goodsService.rollbackGoodNumber(msgContentMap);
    }
}

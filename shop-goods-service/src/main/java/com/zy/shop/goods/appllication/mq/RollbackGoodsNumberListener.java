package com.zy.shop.goods.appllication.mq;

/**
 * @author: jogin
 * @date: 2020/12/6 13:02
 */

import com.zy.shop.goods.appllication.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic}", consumerGroup = "${mq.order.consumer.group.name}", messageModel = MessageModel.BROADCASTING)
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
        try {
            Map<String, String> msgContentMap = new HashMap<>();
            msgContentMap.put("msgId", messageExt.getMsgId());
            msgContentMap.put("msgTag", messageExt.getTags());
            msgContentMap.put("msgKey", messageExt.getKeys());
            msgContentMap.put("msgGroup", groupName);
            msgContentMap.put("msgBoy", new String(messageExt.getBody(), "UTF-8"));
            goodsService.rollbackGoodNumber(msgContentMap);
        } catch (UnsupportedEncodingException e) {
            log.error("RocketMQ 消息 UTF-8 编码失败：{}", e.getMessage(), e);
        }
    }
}

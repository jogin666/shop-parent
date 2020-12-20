package com.zy.shop.user.application.mq;

import com.alibaba.fastjson.JSON;
import com.zy.shop.common.dto.mq.MQMessageEntity;
import com.zy.shop.common.enums.ShopPaidStatusEnum;
import com.zy.shop.pojo.ShopUser;
import com.zy.shop.pojo.ShopUserUseMoneyLog;
import com.zy.shop.user.application.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

/**
 * @Author Jong
 * @Date 2020/12/19 15:09
 * @Version 1.0
 */

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.cancel.topic}",consumerGroup = "${mq.order.cancel.consumer.group}",messageModel = MessageModel.BROADCASTING )
public class RollbackUserMoneyListener implements RocketMQListener<MessageExt> {

    @Autowired
    private IUserService userService;

    @Override
    public void onMessage(MessageExt message) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        MQMessageEntity msgEntity = JSON.parseObject(body, MQMessageEntity.class);
        log.info("rocketmq 接收到消息：{}",body);
        if(msgEntity.getUseMoney()!=null && msgEntity.getUseMoney().compareTo(BigDecimal.ZERO)>0){
            ShopUser shopUser = userService.findOneById(msgEntity.getUserId());
            shopUser.setMoney(shopUser.getMoney().add(msgEntity.getUseMoney()));
            ShopUserUseMoneyLog userMoneyLog = new ShopUserUseMoneyLog();
            userMoneyLog.setUserId(msgEntity.getUserId());
            userMoneyLog.setType(ShopPaidStatusEnum.SHOP_PAY_STATUS_CANCEL.getCode());
            userService.updateUserMoney(shopUser, userMoneyLog);
            log.info("用户:{} 金额：{} 回退成功",msgEntity.getUserId(),msgEntity.getUseMoney());
        }else{
            log.warn("用户:{} 金额：{} 回退失败",msgEntity.getUserId(),msgEntity.getUseMoney());
        }
    }
}

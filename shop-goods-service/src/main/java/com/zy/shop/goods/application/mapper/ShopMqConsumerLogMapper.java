package com.zy.shop.goods.application.mapper;

import com.zy.shop.pojo.ShopMQConsumerLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Jong
 * @Date: 2020/12/5 18:47
 */

@Mapper
public interface ShopMqConsumerLogMapper {

    int saveMqConsumerLog(ShopMQConsumerLog log);

    ShopMQConsumerLog findOneByMsgKey(String msgKey);

    //int updateMqConsumeTime(int times);

    int updateMqConsumerLog(ShopMQConsumerLog log);
}

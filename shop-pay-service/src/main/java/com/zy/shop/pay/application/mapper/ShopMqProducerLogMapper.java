package com.zy.shop.pay.application.mapper;

import com.zy.shop.pojo.ShopMQProducerLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: jogin
 * @date: 2020/12/6 14:02
 */
@Mapper
public interface ShopMqProducerLogMapper {

    int updateProducerLog(ShopMQProducerLog producerLog);

    int saveProducerLog(ShopMQProducerLog producerLog);
}

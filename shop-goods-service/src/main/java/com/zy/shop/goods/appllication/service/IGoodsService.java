package com.zy.shop.goods.appllication.service;

import com.zy.shop.common.exception.ShopBizException;
import com.zy.shop.pojo.ShopGoods;
import com.zy.shop.pojo.ShopMQConsumerLog;

import java.util.Map;

/**
 * @author: jogin
 * @date: 2020/12/5 18:53
 */
public interface IGoodsService {

    ShopGoods findOneById(Long goodsId);

    boolean reduceGoodsNumber(ShopGoods goods,Long orderId) throws ShopBizException;

    boolean rollbackGoodNumber(Map<String,String> msgContentMap);
}

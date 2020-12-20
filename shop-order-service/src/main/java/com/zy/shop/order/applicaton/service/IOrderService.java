package com.zy.shop.order.applicaton.service;

import com.zy.shop.common.exception.ShopBizException;
import com.zy.shop.pojo.ShopOrder;

/**
 * @Author: Jong
 * @Date: 2020/12/6 16:16
 */
public interface IOrderService {

    Boolean saveOrder(ShopOrder order) throws ShopBizException;

    Boolean confirmOrder(ShopOrder order) throws ShopBizException;

    Boolean cancelOrder(ShopOrder shopOrder) throws ShopBizException;
}

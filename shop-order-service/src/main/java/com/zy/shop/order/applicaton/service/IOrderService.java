package com.zy.shop.order.applicaton.service;

import com.zy.shop.common.exception.ShopBizException;
import com.zy.shop.pojo.ShopOrder;

/**
 * @author: jogin
 * @date: 2020/12/6 16:16
 */
public interface IOrderService {

    boolean saveOrder(ShopOrder order) throws ShopBizException;

    boolean confirmOrder(ShopOrder order) throws ShopBizException;
}

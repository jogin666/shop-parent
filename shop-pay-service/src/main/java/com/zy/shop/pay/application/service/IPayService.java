package com.zy.shop.pay.application.service;

import com.zy.shop.common.exception.ShopBizException;
import com.zy.shop.pojo.ShopPay;

/**
 * @author: jogin
 * @date: 2020/12/6 14:05
 */
public interface IPayService {

    boolean createPayment(ShopPay tradePay) throws ShopBizException;

    boolean callbackPayment(ShopPay tradePay) throws ShopBizException;
}

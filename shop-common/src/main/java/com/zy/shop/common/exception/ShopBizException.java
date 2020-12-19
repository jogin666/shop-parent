package com.zy.shop.common.exception;

/**
 * @Author: Jong
 * @Date: 2020/12/5 15:25
 */
public class ShopBizException extends BaseShopException{

    public ShopBizException() {
    }

    public ShopBizException(String message) {
        super(message);
    }

    public ShopBizException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.zy.shop.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Jong
 * @Date: 2020/12/5 15:24
 */
@Setter
@Getter
public class BaseShopException extends Exception {

    protected String message;
    protected Throwable cause;

    public BaseShopException(){

    }

    public BaseShopException(String message) {
        super(message);
        this.message = message;
    }

    public BaseShopException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.cause = cause;
    }
}

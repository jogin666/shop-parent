package com.zy.shop.common.dto.response;

import lombok.Getter;

import java.io.Serializable;

/**
 * @Author: Jong
 * @Date: 2020/12/5 14:59
 */

@Getter
public class BaseShopResponse<T> implements Serializable {

    private final static Integer SUCCESS = 200;
    private final static Integer FAILURE = 500;
    private final static String FAILURE_MSG = " fail ";
    private final static String SUCCESS_MSG = "success";

    private Integer code;
    private T data;
    private String message;

    public BaseShopResponse() {
    }

    public BaseShopResponse(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> BaseShopResponse<T> success(T data) {
        return new BaseShopResponse<>(SUCCESS, data, SUCCESS_MSG);
    }

    public static <T> BaseShopResponse<T> success(T data, String message) {
        return new BaseShopResponse<>(SUCCESS, data, message);
    }

    public static <T> BaseShopResponse<T> fail(T data) {
        return new BaseShopResponse<>(FAILURE, data, FAILURE_MSG);
    }

    public static <T> BaseShopResponse<T> fail(String message) {
        return new BaseShopResponse<>(FAILURE, null, message);
    }

    public static <T> BaseShopResponse<T> fail(T data, String message) {
        return new BaseShopResponse<>(FAILURE, data, message);
    }
}

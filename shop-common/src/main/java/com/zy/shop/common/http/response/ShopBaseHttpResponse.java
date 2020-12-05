package com.zy.shop.common.http.response;

import lombok.Getter;

/**
 * @author: jogin
 * @date: 2020/12/5 15:19
 */
@Getter
public class ShopBaseHttpResponse<T> {

    private final static Integer SUCCESS = 200;
    private final static Integer FAILURE = 500;
    private final static String FAILURE_MSG = " fail ";
    private final static String SUCCESS_MSG = "success";

    private Integer code;
    private T data;
    private String message;

    public ShopBaseHttpResponse() {
    }

    public ShopBaseHttpResponse(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> ShopBaseHttpResponse<T> success(T data) {
        return new ShopBaseHttpResponse<>(SUCCESS, data, SUCCESS_MSG);
    }

    public static <T> ShopBaseHttpResponse<T> success(T data, String message) {
        return new ShopBaseHttpResponse<>(SUCCESS, data, message);
    }

    public static <T> ShopBaseHttpResponse<T> fail(T data) {
        return new ShopBaseHttpResponse<>(FAILURE, data, FAILURE_MSG);
    }

    public static <T> ShopBaseHttpResponse<T> fail(String message) {
        return new ShopBaseHttpResponse<>(FAILURE, null, message);
    }

    public static <T> ShopBaseHttpResponse<T> fail(T data, String message) {
        return new ShopBaseHttpResponse<>(FAILURE, null, message);
    }
}

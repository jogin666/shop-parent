package com.zy.shop.common.http.request;

import lombok.Data;

/**
 * @Author: Jong
 * @Date: 2020/12/5 15:19
 */
@Data
public class BaseShopHttpRequest<T> {

    private T data;
    private String requestId;
}

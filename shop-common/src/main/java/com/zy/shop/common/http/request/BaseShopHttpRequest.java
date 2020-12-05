package com.zy.shop.common.http.request;

import lombok.Data;

/**
 * @author: jogin
 * @date: 2020/12/5 15:19
 */
@Data
public class BaseShopHttpRequest<T> {

    private T data;
    private String requestId;
}

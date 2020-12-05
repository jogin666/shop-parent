package com.zy.shop.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: jogin
 * @date: 2020/12/5 14:56
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseShopRequest<T> implements Serializable {

    private T data;
    private String requestId;
}

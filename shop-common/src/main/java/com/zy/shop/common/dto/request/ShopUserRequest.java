package com.zy.shop.common.dto.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: jogin
 * @date: 2020/12/5 14:56
 */
@Data
public class ShopUserRequest implements Serializable {

    private Long userId;

    private String name;

    private Long account;

    private String password;

    private Integer age;

    private BigDecimal money;

    private String telPhone;

    private String address;
}

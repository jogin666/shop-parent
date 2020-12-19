package com.zy.shop.common.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Jong
 * @Date: 2020/12/5 14:59
 */

@Data
@Builder
public class ShopUserResponse implements Serializable {

    private Long userId;

    private String name;

    private Long account;

    private String password;

    private Integer age;

    private BigDecimal money;

    private String telPhone;

    private String address;
}

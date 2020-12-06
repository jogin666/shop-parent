package com.zy.shop.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: jogin
 * @date: 2020/12/5 13:47
 */

@Data
public class ShopUser implements Serializable {

    private Long userId;

    private String name;

    private Long account;

    private String password;

    private Integer age;

    private BigDecimal money;

    private String telPhone;

    private String address;

}

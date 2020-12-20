package com.zy.shop.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author: Jong
 * @Date: 2020/12/5 13:50
 */

@Data
public class ShopCoupon implements Serializable {

    private Long userId;

    private Long couponId;

    private BigDecimal couponMoney;

    private Integer status;

    private Timestamp createTime;

    private Timestamp useTime;

    private Timestamp updateTime;
}

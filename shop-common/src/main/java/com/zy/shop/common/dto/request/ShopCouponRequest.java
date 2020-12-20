package com.zy.shop.common.dto.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author: Jong
 * @Date: 2020/12/5 14:56
 */

@Data
public class ShopCouponRequest implements Serializable {

    private Long userId;

    private Long couponId;

    private BigDecimal couponMoney;

    private Integer status;

    private Timestamp createTime;

    private Timestamp useTime;

    private Timestamp updateTime;
}

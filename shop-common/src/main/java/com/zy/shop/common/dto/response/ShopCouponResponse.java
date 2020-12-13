package com.zy.shop.common.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author: jogin
 * @date: 2020/12/5 14:59
 */

@Data
@Builder
public class ShopCouponResponse implements Serializable {
    private Long userId;

    private Long couponId;

    private BigDecimal couponMoney;

    private Integer status;

    private Timestamp createTime;

    private Timestamp useTime;

    private Timestamp updateTime;
}

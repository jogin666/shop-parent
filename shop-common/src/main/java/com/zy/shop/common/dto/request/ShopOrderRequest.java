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
public class ShopOrderRequest implements Serializable {

    private Long userId;

    private Long orderId;

    private Long payId;

    private Long couponId;

    private String userName;

    private String telPhone;

    private Integer status;

    private Timestamp createTime;

    private Timestamp upDateTime;

    private Integer goodsNumber;

    private BigDecimal totalMoney;

    private Long goodsId;

    private BigDecimal goodsPrice;

    private BigDecimal couponMoney;

    private BigDecimal moneyPaid;

    private String address;
}

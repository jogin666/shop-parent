package com.zy.shop.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author: Jong
 * @Date: 2020/12/5 13:57
 */

@Data
public class ShopOrder implements Serializable {

    private Long userId;

    private Long orderId;

    private Long payId;

    private Long goodsId;

    private Long couponId;

    private String userName;

    private String goodsName;

    private BigDecimal goodsPrice;

    private Integer goodsNumber;

    private BigDecimal couponMoney;

    private BigDecimal paidMoney;

    private BigDecimal payAmount;

    private BigDecimal totalMoney;

    private Integer orderStatus;

    private String telPhone;

    private String address;

    private Timestamp createTime;

    private Timestamp upDateTime;

    private BigDecimal shippingFee;
}

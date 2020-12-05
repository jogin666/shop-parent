package com.zy.shop.common.dto.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author: jogin
 * @date: 2020/12/5 14:56
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

    private Timestamp updateTime;

    private Integer goodsNumber;

    private BigDecimal totalMoney;

    private String address;
}

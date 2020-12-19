package com.zy.shop.common.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author: Jong
 * @Date: 2020/12/5 14:59
 */

@Data
public class ShopOrderResponse implements Serializable {

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

    private String address;
}

package com.zy.shop.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author: jogin
 * @date: 2020/12/5 13:57
 */

@Data
public class ShopOrder implements Serializable {

    private Long userId;

    private Long orderId;

    private Long payId;

    private Long goodsId;

    private Long couponId;

    private String userName;

    private String telPhone;

    private Integer status;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Integer goodsNumber;

    private BigDecimal goodsPrice;

    private BigDecimal couponMoney;

    private BigDecimal moneyPaid;

    private BigDecimal totalMoney;

    private String address;
}

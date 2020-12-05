package com.zy.shop.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author: jogin
 * @date: 2020/12/5 13:55
 */
@Data
public class ShopPay implements Serializable {

    private Long payId;

    private Long orderId;

    private Long tradeSerial;

    private BigDecimal payMoney;

    private Integer status;

    private Timestamp createTime;

    private Timestamp updateTime;
}

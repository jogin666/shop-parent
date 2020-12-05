package com.zy.shop.common.dto.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author: jogin
 * @date: 2020/12/5 14:56
 */

@Data
public class ShopPayRequest implements Serializable {

    private Long payId;

    private Long orderId;

    private Long tradeSerial;

    private BigDecimal payMoney;

    private Integer status;

    private Timestamp createTime;

    private Timestamp updateTime;
}

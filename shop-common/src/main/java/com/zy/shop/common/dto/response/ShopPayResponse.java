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
public class ShopPayResponse implements Serializable {

    private Long payId;

    private Long orderId;

    private Long tradeSerial;

    private BigDecimal payMoney;

    private Integer status;

    private Timestamp createTime;

    private Timestamp updateTime;
}

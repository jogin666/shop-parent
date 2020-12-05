package com.zy.shop.common.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author: jogin
 * @date: 2020/12/5 14:59
 */
@Data
public class ShopUserUseMoneyLogRequest {

    private Long userId;

    private Long orderId;

    private BigDecimal money;

    private Integer type;

    private Timestamp createTime;

    private Timestamp updateTime;
}

package com.zy.shop.common.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author: Jong
 * @Date: 2020/12/5 14:59
 */
@Data
public class ShopUserUseMoneyLogRequest {

    private Long userId;

    private Long orderId;

    private BigDecimal money;

    private Integer type;

    private Timestamp createTime;

    private Timestamp upDateTime;
}

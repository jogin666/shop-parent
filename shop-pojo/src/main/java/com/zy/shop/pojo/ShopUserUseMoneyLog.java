package com.zy.shop.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author: Jong
 * @Date: 2020/12/5 15:06
 */

@Data
public class ShopUserUseMoneyLog {

    private Long userId;

    private Long orderId;

    private BigDecimal money;

    private Integer type;

    private Timestamp createTime;

    private Timestamp upDateTime;
}

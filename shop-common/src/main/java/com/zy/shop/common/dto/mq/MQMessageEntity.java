package com.zy.shop.common.dto.mq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: jogin
 * @date: 2020/12/5 14:56
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MQMessageEntity implements Serializable {

    // 订单 id
    private Long orderId;
    // 优惠卷 id
    private Long couponId;
    // 用户 id
    private Long userId;
    // 商品 id
    private Long goodId;
    // 商品数量
    private Integer goodNumber;
    // 金额
    private BigDecimal useMoney;
}

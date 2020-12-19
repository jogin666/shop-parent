package com.zy.shop.common.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author: Jong
 * @Date: 2020/12/5 14:59
 */

@Data
@Builder
public class ShopGoodsResponse implements Serializable{

    private Long goodsId;

    private String name;

    private Integer number;

    private BigDecimal goodsPrice;

    private Timestamp createTime;

    private Timestamp upDateTime;

    private String goodsDesc;
}

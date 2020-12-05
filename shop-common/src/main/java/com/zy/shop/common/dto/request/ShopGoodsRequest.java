package com.zy.shop.common.dto.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author: jogin
 * @date: 2020/12/5 14:56
 */

@Data
public class ShopGoodsRequest implements Serializable {

    private Long goodsId;

    private String name;

    private Integer number;

    private BigDecimal goodsPrice;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String goodsDesc;
}

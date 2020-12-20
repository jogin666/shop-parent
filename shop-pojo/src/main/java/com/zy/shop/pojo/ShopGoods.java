package com.zy.shop.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author: Jong
 * @Date: 2020/12/5 13:53
 */

@Data
public class ShopGoods implements Serializable {

    private Long goodsId;

    private String name;

    private Integer number;

    private BigDecimal goodsPrice;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String goodsDesc;

}

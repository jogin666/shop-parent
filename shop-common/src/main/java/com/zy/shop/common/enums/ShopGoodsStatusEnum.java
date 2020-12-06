package com.zy.shop.common.enums;

import lombok.Getter;

/**
 * @author: jogin
 * @date: 2020/12/5 14:35
 */
@Getter
public enum ShopGoodsStatusEnum {

    SHOP_GOODS_STATUS_NOT_FOUND(100, "查询不到商品"),
    SHOP_GOODS_STATUS_NUMBER_INSUFFICIENT(101, "商品库存不足"),
    SHOP_GOOD_STATUS_NUMBER_REDUCING(104, "商品库存扣减中"),
    SHOP_GOOD_STATUS_REDUCE_SUCCESS(102, "商品库存扣成功"),
    SHOP_GOOD_STATUS_REDUCE_FAIL(103, "商品库存扣失败"),
    SHOP_GOOD_STATUS_PRICE_INVALID(104, "商品价格有误"),
    ;

    private Integer code;
    private String des;

    ShopGoodsStatusEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", des='" + des + '\'' +
                '}';
    }
}

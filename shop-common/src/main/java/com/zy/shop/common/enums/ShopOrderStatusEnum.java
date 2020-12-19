package com.zy.shop.common.enums;

import lombok.Getter;

/**
 * @Author: Jong
 * @Date: 2020/12/5 14:39
 */

@Getter
public enum ShopOrderStatusEnum {

    SHOP_ORDER_STATUS_NO_CONFIRM(600, "商品订单未确认"),
    SHOP_ORDER_STATUS_CONFIRM( 601, "商品订单已经确认"),
    SHOP_ORDER_STATUS_CANCEL( 602, "商品订单已取消"),
    SHOP_ORDER_STATUS_INVALID( 603, "商品订单无效"),
    SHOP_ORDER_STATUS_RETURNED( 604, "商品订单已退货"),
    SHOP_ORDER_STATUS_NOT_FOUND( 605, "商品订单不存在"),
    SHOP_ORDER_STATUS_CONFIRM_FAIL( 606, "商品订单确认失败，订单已存在"),
    SHOP_ORDER_STATUS_EXIT(607,"商品订单已存在"),
    SHOP_ORDER_STATUS_CONFIRM_PROCESSING( 608, "商品正在确认"),
    ;

    private final Integer code;
    private final String desc;

    ShopOrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}

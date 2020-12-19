package com.zy.shop.common.enums;

/**
 * @Author: Jong
 * @Date: 2020/12/6 16:35
 */
public enum ShopUserStatusEnum {

    SHOP_USER_STATUS_NO_EXIST(900,"用户不存在"),
    SHOP_USER_STATUS_MONEY_PAID_LESS_ZERO(901,"用户可用金额小于 0 "),
    SHOP_USER_STATUS_MONEY_PAID_INVALID(902,"用户支付的金额大于可用余额")
    ;
    private final Integer code;
    private final String des;

    ShopUserStatusEnum(Integer code, String des) {
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

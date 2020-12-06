package com.zy.shop.common.enums;

import lombok.Getter;

/**
 * @author: jogin
 * @date: 2020/12/5 14:29
 */

@Getter
public enum ShopCouponStatusEnum {

    SHOP_COUPON_INVALID(300, "优惠券不合法"),
    SHOP_COUPON_USE_FAIL(301, "优惠券使用失败"),
    SHOP_COUPON_NO_EXIST(302, "优惠券不存在"),
    SHOP_COUPON_USED(303, "优惠券已使用"),
    SHOP_COUPON_UNUSED(304, "优惠券未使用"),
    SHOP_COUPON_UPDATE_SUCCESS(305, "优惠券状态更新成功"),
    SHOP_COUPON_UPDATE_FAIL(306, "优惠券状态更新失败"),
    ;
    private Integer code;
    private String desc;

    ShopCouponStatusEnum(Integer code, String desc) {
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

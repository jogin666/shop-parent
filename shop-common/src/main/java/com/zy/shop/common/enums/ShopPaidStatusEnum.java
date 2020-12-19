package com.zy.shop.common.enums;

import lombok.Getter;

/**
 * @Author: Jong
 * @Date: 2020/12/5 14:05
 */

@Getter
public enum ShopPaidStatusEnum {
    SHOP_PAY_STATUS_UNPAID(701, "订单待支付"),
    SHOP_PAY_STATUS_PAID(702, "订单已支付"),
    SHOP_PAY_STATUS_CANCEL(703, "订单已取消"),
    SHOP_PAY_STATUS_DISCARD(704, "订单已作废"),
    SHOP_PAY_STATUS_FAILURE(705, "订单支付失败"),
    SHOP_PAY_STATUS_REFUND(706, "订单待退款"),
    SHOP_PAY_STATUS_ROLLBACK_SUCCESS(707, "订单退款成功"),
    SHOP_PAY_STATUS_ROLLBACK_FAIL1(708, "退款失败，订单已退过款"),
    SHOP_PAY_STATUS_ROLLBACK_FAIL2(709, "退款失败，订单未支付"),
    SHOP_PAY_STATUS_ERROR(710, "支付失败，订单状态时不是未支付"),
    SHOP_PAY_STATUS_CANNOT_CREATE(711, "订单创建失败"),
    SHOP_PAY_STATUS_NOT_FOUND(712,"订单查询不到"),
    ;

    private final Integer code;
    private final String desc;

    ShopPaidStatusEnum(Integer code, String desc) {
        this.desc = desc;
        this.code = code;
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}

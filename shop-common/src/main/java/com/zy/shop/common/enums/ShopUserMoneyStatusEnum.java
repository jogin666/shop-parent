package com.zy.shop.common.enums;

import lombok.Getter;

/**
 * @author: jogin
 * @date: 2020/12/5 14:45
 */

@Getter
public enum ShopUserMoneyStatusEnum {

    SHOP_USER_MONEY_STATUS_REDUCE_SUCCESS(800,"用户金额扣减成功"),
    SHOP_USER_MONEY_STATUS_REDUCE_FAIL(801,"用户金额扣减失败"),
    SHOP_USER_MONEY_STATUS_ROLLBACK_SUCCESS(802,"用户支付金额回滚成功"),
    SHOP_USER_MONEY_STATUS_ROLLBACK_FAIL(803,"用户支付金额回滚失败"),
    SHOP_USER_MONEY_STATUS_REDUCING(804,"用户金额扣减进行中"),
    SHOP_USER_MONEY_STATUS_ROLLBACKING(805,"用户金额退款进行中"),
    SHOP_USER_MONEY_STATUS_UPDATING(806,"用户金额更新进行中"),
    ;

    private Integer code;
    private String desc;

    ShopUserMoneyStatusEnum(Integer code, String desc) {
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

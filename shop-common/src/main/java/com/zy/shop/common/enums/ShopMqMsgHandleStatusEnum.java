package com.zy.shop.common.enums;

import lombok.Getter;

/**
 * @author: jogin
 * @date: 2020/12/5 14:16
 */

@Getter
public enum ShopMqMsgHandleStatusEnum {

    SHOP_MQ_MSG_STATUS_PROCESSING(201, "消息正在处理"),
    SHOP_MQ_MSG_STATUS_SUCCESS(202, "消息处理成功"),
    SHOP_MQ_MSG_STATUS_FAIL(203, "消息处理失败"),
    ;

    private Integer status;
    private String desc;

    ShopMqMsgHandleStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "{" +
                "status=" + status +
                ", desc='" + desc + '\'' +
                '}';
    }
}

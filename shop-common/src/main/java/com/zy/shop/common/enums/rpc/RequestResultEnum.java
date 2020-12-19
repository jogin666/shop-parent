package com.zy.shop.common.enums.rpc;

import lombok.Getter;

/**
 * @Author: Jong
 * @Date: 2020/12/5 14:49
 */

@Getter
public enum RequestResultEnum {

    REQUEST_PARAM_EMPTY(100,"请求参数不能为空"),
    REQUEST_PARAM_ERROR(101,"请求参数有误"),
    REQUEST_REPEAT_SUBMIT(102,"请勿短时间内重复提交请求"),
    REQUEST_RESULT_SUCCESS(103,"请求执行成功"),
    REQUEST_RESULT_FAIL(104,"请求执行失败")
    ;

    private final Integer code;
    private final String des;

    RequestResultEnum(Integer code, String des) {
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

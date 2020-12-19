package com.zy.shop.common.util;

/**
 * @Author: Jong
 * @Date: 2020/12/5 14:56
 */
public class ResultBuilder {

    private final static String CONDITION_EMPTY_PREFIX = "请求参数 ";
    private final static String CONDITION_EMPTY_SUFFIX = "为空！";

    public static String conditionEmpty(String condition) {
        return CONDITION_EMPTY_PREFIX +
                condition +
                CONDITION_EMPTY_SUFFIX;
    }
}

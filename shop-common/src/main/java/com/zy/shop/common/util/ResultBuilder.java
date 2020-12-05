package com.zy.shop.common.util;

/**
 * @author: jogin
 * @date: 2020/12/5 14:56
 */
public class ResultBuilder {

    private final static String CONDITION_EMPTY_PREFIX = "请求参数 ";
    private final static String CONDITION_EMPTY_SUFFIX = "为空！";

    public static String conditionEmpty(String condition) {
        StringBuilder builder = new StringBuilder();
        builder.append(CONDITION_EMPTY_PREFIX)
                .append(condition)
                .append(CONDITION_EMPTY_PREFIX);
        return builder.toString();
    }
}

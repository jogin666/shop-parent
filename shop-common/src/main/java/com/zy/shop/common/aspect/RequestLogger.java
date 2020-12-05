package com.zy.shop.common.aspect;

import java.lang.annotation.*;

/**
 * @author: jogin
 * @date: 2020/12/5 14:56
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestLogger {

    String description();
}

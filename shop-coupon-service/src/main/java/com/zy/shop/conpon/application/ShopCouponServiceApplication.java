package com.zy.shop.conpon.application;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: jogin
 * @date: 2020/12/5 15:31
 */

@SpringBootApplication
@EnableDubbo
public class ShopCouponServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopCouponServiceApplication.class,args);
    }
}

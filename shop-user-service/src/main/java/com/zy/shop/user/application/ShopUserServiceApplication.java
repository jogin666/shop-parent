package com.zy.shop.user.application;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: jogin
 * @date: 2020/12/6 14:50
 */

@EnableDubbo
@SpringBootApplication
public class ShopUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopUserServiceApplication.class,args);
    }
}

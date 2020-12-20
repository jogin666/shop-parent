package com.zy.shop.user.application;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: Jong
 * @Date: 2020/12/6 14:50
 */

@EnableDubbo
@SpringBootApplication
@EnableTransactionManagement
public class ShopUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopUserServiceApplication.class,args);
    }
}

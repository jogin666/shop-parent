package com.zy.shop.pay.application;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: Jong
 * @Date: 2020/12/6 13:58
 */

@EnableDubbo
@SpringBootApplication
@EnableTransactionManagement
public class ShopPayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopPayServiceApplication.class,args);
    }
}

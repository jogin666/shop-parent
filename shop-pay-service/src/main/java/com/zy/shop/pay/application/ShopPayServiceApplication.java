package com.zy.shop.pay.application;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: jogin
 * @date: 2020/12/6 13:58
 */

@EnableDubbo
@SpringBootApplication
public class ShopPayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopPayServiceApplication.class,args);
    }
}

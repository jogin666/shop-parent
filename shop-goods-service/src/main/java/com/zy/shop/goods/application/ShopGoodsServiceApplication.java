package com.zy.shop.goods.application;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: jogin
 * @date: 2020/12/5 18:37
 */

@EnableDubbo
@SpringBootApplication
public class ShopGoodsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopGoodsServiceApplication.class,args);
    }
}

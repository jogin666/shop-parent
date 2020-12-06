package com.zy.shop.order.applicaton;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: jogin
 * @date: 2020/12/6 16:12
 */

@EnableDubbo
@SpringBootApplication
public class ShopOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopOrderServiceApplication.class,args);
    }
}

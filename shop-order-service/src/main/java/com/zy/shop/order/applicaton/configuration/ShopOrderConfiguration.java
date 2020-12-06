package com.zy.shop.order.applicaton.configuration;

import com.zy.shop.common.util.IDWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: jogin
 * @date: 2020/12/6 16:50
 */

@Configuration
public class ShopOrderConfiguration {

    @Bean
    public IDWorker getBean(){
        return new IDWorker(1,2);
    }
}

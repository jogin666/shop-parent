package com.zy.shop.goods.appllication.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author: jogin
 * @date: 2020/12/6 12:55
 */

@Configuration
public class GoodsServiceConfiguration {

    @Bean("poolTaskExecutor")
    public ThreadPoolTaskExecutor newThreadPoolTaskExecutor(){
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(5);
        poolTaskExecutor.setMaxPoolSize(5);
        poolTaskExecutor.setKeepAliveSeconds(3000);
        return poolTaskExecutor;
    }
}

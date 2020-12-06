package com.zy.shop.pay.application.configuration;

import com.zy.shop.common.util.IDWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author: jogin
 * @date: 2020/12/6 14:23
 */

@Configuration
public class ShopPayConfiguration {

    @Bean("poolTaskExecutor")
    public ThreadPoolTaskExecutor newThreadPoolTaskExecutor(){
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(5);
        poolTaskExecutor.setMaxPoolSize(5);
        poolTaskExecutor.setKeepAliveSeconds(3000);
        return poolTaskExecutor;
    }

    @Bean
    public IDWorker getBean(){
        return new IDWorker(1,2);
    }
}

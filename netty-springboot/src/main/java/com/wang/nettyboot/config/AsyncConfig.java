package com.wang.nettyboot.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: 线程池配置
 * @author: wei·man cui
 * @date: 2021/4/30 16:36
 */
@Configuration
public class AsyncConfig {

    @Bean
    public Executor executorService() {
        return new ThreadPoolExecutor(50, 200, 30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(528),
                new ThreadFactoryBuilder().setNameFormat("EventBus-thread").build());
    }

}

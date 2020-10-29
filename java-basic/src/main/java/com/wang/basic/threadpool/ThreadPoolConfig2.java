package com.wang.basic.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: 基于Spring 的线程池配置 {@link ThreadPoolTaskExecutor}
 * @author: wei·man cui
 * @date: 2020/10/29 9:20
 * @see com.wang.basic.threadpool.ThreadPoolConfig2Test
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig2 {

    private int corePoolSize = 5;

    private int maxPoolSize = 10;

    private int queueCapacity = 20;

    private String threadNamePrefix = "Business-ThreadPool-";

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }


}

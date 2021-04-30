package com.wang.nettyboot.config;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * @description: EventBus 框架异步处理 业务逻辑
 * @author: wei·man cui
 * @date: 2021/4/30 15:03
 */
@Configuration
public class EventBusConfig {

    @Resource
    private Executor executor;

    @Bean(name = "eventBus")
    public EventBus registerEventBus() {
        final AsyncEventBus eventBus = new AsyncEventBus("EventBusName", executor);
        // 订阅者 构成单例，直接注册到 发布者中
        eventBus.register(eventSubscribe());
        return eventBus;
    }

    @Bean(name = "eventSubscribe")
    public EventSubscribe eventSubscribe() {
        return new EventSubscribe();
    }

}

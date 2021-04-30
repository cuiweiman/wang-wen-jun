package com.wang.nettyboot.config;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: EventBus 订阅者，处理逻辑
 * @author: wei·man cui
 * @date: 2021/4/30 15:05
 */
@Slf4j
public class EventSubscribe {

    @Subscribe
    @AllowConcurrentEvents
    public void processEvent(String event) {
        log.info("[EventSubscribe#processEvent] 处理业务逻辑：{}", event);
    }

}

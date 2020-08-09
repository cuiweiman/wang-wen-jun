package com.wang.guava.eventbus.service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.wang.guava.eventbus.events.Request;
import com.wang.guava.eventbus.events.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 事件解耦 Demo。Listener。
 * @date: 2020/8/9 23:52
 * @author: wei·man cui
 */
@Slf4j
public class QueryService {
    private final EventBus eventBus;

    public QueryService(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    public void query(String orderNo) {
        log.info("【Received】the orderNo [{}]", orderNo);
        eventBus.post(new Request(orderNo));
    }

    @Subscribe
    public void handlerResponse(Response response) {
        log.info("【Received】{}", response.toString());
    }

}

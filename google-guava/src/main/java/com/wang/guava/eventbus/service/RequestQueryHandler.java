package com.wang.guava.eventbus.service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.wang.guava.eventbus.events.Request;
import com.wang.guava.eventbus.events.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 事件解耦 Demo
 * @date: 2020/8/9 23:56
 * @author: wei·man cui
 */
@Slf4j
public class RequestQueryHandler {
    private final EventBus eventBus;

    public RequestQueryHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Subscribe
    public void duQuery(Request request) {
        log.info("start query the orderNo [{}]", request.toString());
        Response response = new Response();
        this.eventBus.post(response);
    }
}

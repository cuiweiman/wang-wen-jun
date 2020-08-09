package com.wang.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.wang.guava.eventbus.listeners.MultipleEventListener;

/**
 * @description: 发送多种类型事件 的消息中线 注册中心
 * @date: 2020/8/9 16:20
 * @author: wei·man cui
 */
public class MultipleEventBus {
    public static void main(String[] args) {
        final EventBus bus = new EventBus();
        bus.register(new MultipleEventListener());
        bus.post("STRING EVENT");
        bus.post(123456);
    }
}

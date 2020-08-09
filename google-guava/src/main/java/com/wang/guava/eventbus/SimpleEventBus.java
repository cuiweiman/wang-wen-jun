package com.wang.guava.eventbus;

import com.google.common.eventbus.EventBus;

/**
 * 在这里 将 消费者(listener)注册给 事件总线中
 *
 * @description: EventBus事件总线
 * @date: 2020/8/9 16:15
 * @author: wei·man cui
 */
public class SimpleEventBus {

    public static void main(String[] args) {
        final EventBus eventBus = new EventBus();
        eventBus.register(new SimpleListener());
        System.out.println("post the simple event.");
        // 发送一个 String类型事件 到 EventBus中
        eventBus.post("Simple Event");
    }

}

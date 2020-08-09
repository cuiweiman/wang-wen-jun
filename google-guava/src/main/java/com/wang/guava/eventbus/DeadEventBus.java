package com.wang.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.wang.guava.eventbus.listeners.DeadEventListener;

/**
 * @description: DeadEvent 实例
 * @date: 2020/8/9 23:44
 * @author: wei·man cui
 */
public class DeadEventBus {

    public static void main(String[] args) {
        // Event自定义 toString方法
        final EventBus bus = new EventBus("DeadEventBus") {
            @Override
            public String toString() {
                return "DEAD-EVENT-BUS";
            }
        };
        DeadEventListener listener = new DeadEventListener();
        bus.register(listener);
        bus.post("HELLO");
        // EventBus 取消注册的 订阅者
        bus.unregister(listener);
        bus.post("HELLO");
    }

}

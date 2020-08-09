package com.wang.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.wang.guava.eventbus.listeners.ConcreteListener;

/**
 * @description: Listener之间的继承关系
 * @date: 2020/8/9 16:27
 * @author: wei·man cui
 */
public class InheritListenerEventBusExample {
    public static void main(String[] args) {
        final EventBus bus = new EventBus();
        bus.register(new ConcreteListener());
        bus.post("STRING");
    }
}

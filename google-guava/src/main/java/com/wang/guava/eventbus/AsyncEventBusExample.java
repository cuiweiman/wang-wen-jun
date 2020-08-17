package com.wang.guava.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import com.wang.guava.eventbus.listeners.AsyncEventBusListener;

import java.util.concurrent.Executors;

/**
 * @description: Guava 异步事件总线
 * @author: wei·man cui
 * @date: 2020/8/17 9:42
 */
public class AsyncEventBusExample {

    public static void main(String[] args) {
        AsyncEventBus eventBus = new AsyncEventBus(Executors.newFixedThreadPool(4));
        eventBus.register(new AsyncEventBusListener());
        eventBus.post("hello");
    }

}

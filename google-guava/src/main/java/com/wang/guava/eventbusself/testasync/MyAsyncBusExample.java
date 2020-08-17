package com.wang.guava.eventbusself.testasync;

import com.wang.guava.eventbusself.MyAsyncEventBus;
import com.wang.guava.eventbusself.test.MySimpleListener;
import com.wang.guava.eventbusself.test.MySimpleListener2;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: 异步 测试
 * @author: wei·man cui
 * @date: 2020/8/17 9:39
 */
public class MyAsyncBusExample {

    public static void main(String[] args) {
        MyAsyncEventBus asyncBus = new MyAsyncEventBus((ThreadPoolExecutor) Executors.newFixedThreadPool(4));
        asyncBus.register(new MySimpleListener());
        asyncBus.register(new MySimpleListener2());

        asyncBus.post(456, "async-topic");
        asyncBus.post("Event");
    }

}

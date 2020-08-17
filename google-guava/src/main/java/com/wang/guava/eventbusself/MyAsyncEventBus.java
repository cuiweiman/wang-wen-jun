package com.wang.guava.eventbusself;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: 异步 事件总线
 * @author: wei·man cui
 * @date: 2020/8/17 9:33
 */
public class MyAsyncEventBus extends MyEventBus {

    public MyAsyncEventBus(String busName, MyEventExceptionHandler exceptionHandler, ThreadPoolExecutor executor) {
        super(busName, exceptionHandler, executor);
    }

    public MyAsyncEventBus(String busName, ThreadPoolExecutor executor) {
        this(busName, null, executor);
    }

    public MyAsyncEventBus(ThreadPoolExecutor executor) {
        this("default-async", null, executor);
    }

    public MyAsyncEventBus(MyEventExceptionHandler handler, ThreadPoolExecutor executor) {
        this("default-async", handler, executor);
    }
}

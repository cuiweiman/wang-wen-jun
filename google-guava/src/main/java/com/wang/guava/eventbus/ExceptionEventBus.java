package com.wang.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.wang.guava.eventbus.listeners.ExceptionListener;

/**
 * @description: 异常处理 Demo
 * @date: 2020/8/9 23:31
 * @author: wei·man cui
 */
public class ExceptionEventBus {
    public static void main(String[] args) {
        final EventBus bus = new EventBus(new ExceptionHandler());
        bus.register(new ExceptionListener());
        bus.post("EXCEPTION");
    }

    static class ExceptionHandler implements SubscriberExceptionHandler {
        @Override
        public void handleException(Throwable exception, SubscriberExceptionContext context) {
            System.out.println("=====处理订阅者方法发生的异常=====");
            System.out.println("【事件】" + context.getEvent());
            System.out.println("【事件总线（默认default）】" + context.getEventBus());
            System.out.println("【订阅者类】" + context.getSubscriber());
            System.out.println("【订阅者执行方法】" + context.getSubscriberMethod());
        }
    }
}

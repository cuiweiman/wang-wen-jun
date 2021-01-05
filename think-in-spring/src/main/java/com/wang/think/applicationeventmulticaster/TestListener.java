package com.wang.think.applicationeventmulticaster;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @description: Spring 事件监听器的简单使用
 * @date: 2021/1/5 23:14
 * @author: wei·man cui
 */
public class TestListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        TestEvent testEvent = (TestEvent) event;
        testEvent.print();
    }
}

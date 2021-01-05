package com.wang.think.applicationeventmulticaster;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: Spring 事件监听器的简单使用
 * @date: 2021/1/5 23:16
 * @author: wei·man cui
 */
public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("test/applicationeventmulticaster/application-event.xml");
        TestEvent testEvent = new TestEvent("hello", "msg");
        applicationContext.publishEvent(testEvent);

    }
}

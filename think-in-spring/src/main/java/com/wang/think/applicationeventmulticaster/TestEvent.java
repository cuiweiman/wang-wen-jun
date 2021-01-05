package com.wang.think.applicationeventmulticaster;

import org.springframework.context.ApplicationEvent;

/**
 * @description: Spring 事件监听器的简单使用
 * @date: 2021/1/5 23:12
 * @author: wei·man cui
 */
public class TestEvent extends ApplicationEvent {

    private String msg;


    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public TestEvent(Object source) {
        super(source);
    }

    public TestEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }

    public void print() {
        System.out.println(msg);
    }
}

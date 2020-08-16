package com.wang.guava.eventbusself;

import java.lang.reflect.Method;

/**
 * @description: 事件上下文
 * @date: 2020/8/11 23:22
 * @author: wei·man cui
 */
public interface MyEventContext {

    String getSource();

    Object getSubscriber();

    Method getSubscribe();

    Object getEvent();

}

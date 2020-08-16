package com.wang.guava.eventbusself;

import lombok.Getter;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2020/8/14 17:29
 */
@Getter
public class MySubscriber {

    private final Object subscribeObject;

    private final Method subscribeMethod;

    private boolean disable = false;

    public MySubscriber(Object subscribeObject, Method subscribeMethod) {
        this.subscribeObject = subscribeObject;
        this.subscribeMethod = subscribeMethod;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}

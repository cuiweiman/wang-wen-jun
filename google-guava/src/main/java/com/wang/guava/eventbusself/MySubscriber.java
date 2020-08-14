package com.wang.guava.eventbusself;

import lombok.Getter;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2020/8/14 17:29
 */
@Getter
public class MySubscriber {

    private final Object subscribeObject;

    private final Object subscribeMethod;

    public MySubscriber(Object subscribeObject, Object subscribeMethod) {
        this.subscribeObject = subscribeObject;
        this.subscribeMethod = subscribeMethod;
    }
}

package com.wang.guava.eventbusself.test;

import com.wang.guava.eventbusself.MySubscribe;

/**
 * @description: 测试 订阅者
 * @date: 2020/8/16 22:47
 * @author: wei·man cui
 */
public class MySimpleListener {

    @MySubscribe
    public void test1(String x) {
        System.out.println("MySimpleListener === test1 === " + x);
    }

    @MySubscribe(topic = "self-topic")
    public void test2(Integer x) {
        System.out.println("MySimpleListener === test2 === " + x);
    }


}

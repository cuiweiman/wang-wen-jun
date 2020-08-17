package com.wang.guava.eventbusself.test;

import com.wang.guava.eventbusself.MySubscribe;

import java.util.concurrent.TimeUnit;

/**
 * @description: 测试 订阅者
 * @date: 2020/8/16 22:47
 * @author: wei·man cui
 */
public class MySimpleListener2 {

    @MySubscribe
    public void test1(String x) {
        System.out.println("MySimpleListener2 === test1 === " + x);
    }

    @MySubscribe(topic = "self-topic")
    public void test2(Integer x) {
        System.out.println("MySimpleListener2 === test2 === " + x);
    }


    @MySubscribe(topic = "test-topic")
    public void test3(Integer x) {
        throw new RuntimeException();
    }

    /**
     * 同步执行 很慢，可以采用异步的方案
     *
     * @param x 入参
     */
    @MySubscribe(topic = "async-topic")
    public void test4(Integer x) {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("MySimpleListener2 === test4 === " + x);
    }


}

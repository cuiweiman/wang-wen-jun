package com.wang.guava.eventbusself.test;

import com.wang.guava.eventbusself.MyEventBus;

/**
 * @description: 测试 系自定义的统总线
 * @date: 2020/8/16 22:48
 * @author: wei·man cui
 */
public class MyEventBusExample {

    public static void main(String[] args) {
        MyEventBus myEventBus = new MyEventBus();
        myEventBus.register(new MySimpleListener());
        myEventBus.register(new MySimpleListener2());
        // myEventBus.post(" 测试系统总线 ");
        myEventBus.post(123, "self-topic");
        myEventBus.post(456, "async-topic");

        MyEventBus myEventBusException = new MyEventBus((cause, context) -> {
            cause.printStackTrace();
            System.out.println("============分割线============");
            System.out.println(context.getSource());
            System.out.println(context.getSubscribe());
            System.out.println(context.getEvent());
            System.out.println(context.getSubscriber());

        });
        myEventBusException.register(new MySimpleListener2());
        myEventBusException.post(123, "test-topic");
    }

}


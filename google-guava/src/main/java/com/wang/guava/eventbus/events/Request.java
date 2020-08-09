package com.wang.guava.eventbus.events;

/**
 * @description: 事件解耦 Demo
 * @date: 2020/8/9 23:51
 * @author: wei·man cui
 */
public class Request {
    private final String orderNo;

    public Request(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "Request{" +
                "orderNo='" + orderNo + '\'' +
                '}';
    }
}

package com.wang.concurrent.blockingqueue;

import lombok.Getter;

/**
 * 订单实体类
 */
@Getter
public class Order {
    private final String orderNo;
    private final double money;

    public Order(String orderNo, double money) {
        this.orderNo = orderNo;
        this.money = money;
    }
}

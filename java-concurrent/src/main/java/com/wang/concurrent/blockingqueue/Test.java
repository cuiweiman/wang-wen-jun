package com.wang.concurrent.blockingqueue;

import java.util.concurrent.DelayQueue;

/**
 * 测试 延时订单
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {

        DelayQueue<ItemVO<Order>> queue = new DelayQueue<>();

        new Thread(new PutOrder(queue)).start();
        new Thread(new FetchOrder(queue)).start();

        for (int i = 1; i < 15; i++) {
            Thread.sleep(500);
            System.out.println(i * 500);
        }
    }

}

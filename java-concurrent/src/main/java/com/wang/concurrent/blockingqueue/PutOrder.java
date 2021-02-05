package com.wang.concurrent.blockingqueue;

import java.util.concurrent.DelayQueue;

/**
 * 队列中 添加元素
 */
public class PutOrder implements Runnable {

    private DelayQueue<ItemVO<Order>> queue;

    public PutOrder(DelayQueue<ItemVO<Order>> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        // 创建一个 5s到期 元素，并插入队列
        Order orderTb = new Order("TB123", 150);
        ItemVO<Order> itemTb = new ItemVO<>(5000L, orderTb);

        queue.offer(itemTb);
        System.out.println("订单5s后到期：" + orderTb.getOrderNo());

        // 创建一个 8s到期 元素，并插入队列
        Order orderJd = new Order("JD124", 150);
        ItemVO<Order> itemJd = new ItemVO<>(8000L, orderJd);

        queue.offer(itemJd);
        System.out.println("订单8s后到期：" + orderJd.getOrderNo());
    }
}

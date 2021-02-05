package com.wang.concurrent.blockingqueue;

import java.util.concurrent.DelayQueue;

/**
 * 队列中 获取到期的元素
 */
public class FetchOrder implements Runnable {

    private DelayQueue<ItemVO<Order>> queue;

    public FetchOrder(DelayQueue<ItemVO<Order>> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // take是阻塞方法，队列为空时，方法阻塞
                ItemVO<Order> item = queue.take();
                Order order = item.getData();
                System.out.println("get from Queue: " + order.getOrderNo());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

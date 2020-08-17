package com.wang.guava.concurrent;

import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import static java.lang.Thread.currentThread;

/**
 * @description: 高并发限流——漏桶算法
 * @author: wei·man cui
 * @date: 2020/8/17 15:28
 */
public class Bucket {

    /**
     * 桶 容器
     */
    private final ConcurrentLinkedQueue<Integer> container = new ConcurrentLinkedQueue<>();

    /**
     * 上沿 最大 1000 个请求
     */
    private final static int BUCKET_LIMIT = 1000;

    /**
     * 下沿 每秒最多处理 10 个请求
     */
    private final RateLimiter limiter = RateLimiter.create(10);

    /**
     * 判断 容器接收的请求 是否在 最大请求范围内
     */
    private final Monitor offerMonitor = new Monitor();

    /**
     * 容器 接收
     */
    private final Monitor pollMonitor = new Monitor();

    public void submit(Integer data) {
        // 如果 容器 长度小于最大值，则允许 提交请求
        if (offerMonitor.enterIf(offerMonitor.newGuard(() -> container.size() < BUCKET_LIMIT))) {
            try {
                container.offer(data);
                System.out.println(currentThread() + " submit data " + data + ", current size: " + container.size());
            } finally {
                offerMonitor.leave();
            }
        } else {
            // 桶满了，降级处理(简单 抛错)
            throw new IllegalStateException("The Bucket is full.");
        }
    }

    public void takeThenConsumer(Consumer<Integer> consumer) {
        if (pollMonitor.enterIf(pollMonitor.newGuard(() -> !container.isEmpty()))) {
            try {
                System.out.println(currentThread() + " waiting " + limiter.acquire());
                consumer.accept(container.poll());
            } finally {
                pollMonitor.leave();
            }
        }
    }

}

package com.wang.guava.concurrent;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.currentThread;

/**
 * <p>
 * 令牌算法：
 * 1. 接收的请求，需要从令牌桶中获取到令牌，才能够被处理。
 * 2. 固定容量的令牌桶 源源不断地生成令牌，若令牌过多，则溢出桶被抛弃。
 * </p>
 * <p>
 * 模拟 手机饥饿营销：
 *
 * </p>
 *
 * @description: 令牌桶算法 模拟饥饿营销
 * @author: wei·man cui
 * @date: 2020/8/17 16:26
 */
public class TokenBucket {

    private AtomicInteger phoneNumbers = new AtomicInteger(0);

    private final static int LIMIT = 100;

    private RateLimiter rateLimiter = RateLimiter.create(10);

    private final int saleLimit;

    public TokenBucket() {
        this(LIMIT);
    }

    public TokenBucket(int saleLimit) {
        this.saleLimit = saleLimit;
    }

    /**
     * 获取 令牌的 订单，才被处理.
     * 售卖数量 在 限售范围内，则允许继续购买，否则 抛出异常.（判断拿到令牌的订单，是否超过最大限售）。
     */
    public int buy() {
        // 计时
        Stopwatch started = Stopwatch.createStarted();

        // 阻塞等待令牌。最多等待 时间 10 秒钟
        boolean successToken = rateLimiter.tryAcquire(10, TimeUnit.SECONDS);
        // 获取到令牌的 请求，才被 处理
        if (successToken) {
            if (phoneNumbers.get() >= saleLimit) {
                throw new IllegalStateException("Not any phone can be sale, please wait to next time.");
            }
            int phoneNo = phoneNumbers.getAndIncrement();
            this.handleOrder();
            System.out.println(currentThread() + " user get the Mi Phone: " + phoneNo + ", 【耗时】:" + started.stop());
            return phoneNo;
        } else {
            throw new RuntimeException("Sorry,occur exception when buy phone. 【耗时】：" + started.stop());
        }
    }

    /**
     * 处理 订单请求。应该是 异步处理的
     */
    private void handleOrder() {
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

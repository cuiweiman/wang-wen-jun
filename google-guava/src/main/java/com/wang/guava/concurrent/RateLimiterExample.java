package com.wang.guava.concurrent;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.lang.Thread.currentThread;

/**
 * <p>
 * 漏桶算法：（漏桶，进口大出口小）
 * 接收请求不受限制，处理时受限（如：每秒处理五个请求）。
 * </p>
 *
 * @description: RateLimiter 限制每秒执行次数（实现限流等操作），类似于 Semaphore
 * @author: wei·man cui
 * @date: 2020/8/17 15:10
 */
public class RateLimiterExample {

    // 1秒钟，允许多少次操作。0.5次/s，即 1次/2s
    private final static RateLimiter limiter = RateLimiter.create(0.5);

    private final static Semaphore semaphore = new Semaphore(3);

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(10);
        /*IntStream.range(0, 10).forEach(i -> {
            service.submit(RateLimiterExample::testLimiter);
        });*/
        IntStream.range(0, 10).forEach(i -> {
            service.submit(RateLimiterExample::testSemaphore);
        });

    }

    private static void testLimiter() {
        System.out.println(currentThread() + " waition " + limiter.acquire());
    }

    /**
     * 使用 Semaphore 实现
     */
    private static void testSemaphore() {
        try {
            semaphore.acquire();
            System.out.println(currentThread() + " is coming and do work.");
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
            System.out.println(currentThread() + " release the Semaphore");
        }
    }
}

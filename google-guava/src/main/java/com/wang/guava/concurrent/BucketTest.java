package com.wang.guava.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.lang.Thread.currentThread;

/**
 * @description: 高并发限流——漏桶算法 测试
 * @author: wei·man cui
 * @date: 2020/8/17 15:41
 */
public class BucketTest {
    public static void main(String[] args) {
        final Bucket bucket = new Bucket();

        final AtomicInteger DATA_CREATOR = new AtomicInteger(0);

        IntStream.range(0, 5).forEach(i -> new Thread(() -> {
            for (; ; ) {
                int data = DATA_CREATOR.getAndIncrement();
                bucket.submit(data);
                try {
                    TimeUnit.MILLISECONDS.sleep(200L);
                } catch (InterruptedException e) {
                    if (e instanceof InterruptedException) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }).start());

        IntStream.range(0, 5).forEach(i -> new Thread(() -> {
            for (; ; ) {
                bucket.takeThenConsumer(x -> System.out.println(currentThread() + " w " + x));
            }
        }).start());

    }
}

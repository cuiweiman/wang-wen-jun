package com.wang.concurrencyart.chapter1;

/**
 * @description: 并发执行一定比串行执行快么？ of course not.
 * @date: 2021/2/1 22:38
 * @author: wei·man cui
 */
public class Demo1 {
    /**
     * 1,0000：serial=0ms，concurrency=38ms
     * 10,0000：serial=2ms，concurrency=38ms
     * 100,0000：serial=3ms，concurrency=4ms
     * 1000,0000：serial=8ms，concurrency=47ms
     * 1,0000,0000：serial=54ms，concurrency=91ms
     * 10,0000,0000：serial=545ms，concurrency=342ms
     * emm……感觉不如串行嘞还
     */
    private static final long COUNT = 1000000000L;

    public static void main(String[] args) throws InterruptedException {
        concurrency();
        serial();
    }

    private static void concurrency() throws InterruptedException {
        // 并行执行
        long start = System.currentTimeMillis();
        Thread thread = new Thread(() -> {
            long a = 0;
            for (long i = 0; i < COUNT; i++) {
                a += 5;
            }
        });
        thread.start();
        long b = 0;
        for (long i = 0; i < COUNT; i++) {
            b--;
        }
        thread.join();
        long time = System.currentTimeMillis() - start;
        System.out.println("concurrency: " + time + "ms, b=" + b);
    }

    private static void serial() {
        // 串行执行
        long start = System.currentTimeMillis();
        long a = 0;
        for (long i = 0; i < COUNT; i++) {
            a += 5;
        }
        long b = 0;
        for (long i = 0; i < COUNT; i++) {
            b--;
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("serial: " + time + "ms, a=" + a + ", b=" + b);
    }
}

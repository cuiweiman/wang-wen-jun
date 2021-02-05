package com.wang.concurrent.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子类 的使用
 * AtomicInteger
 */
public class AtomicIntegerDemo {

    static AtomicInteger ai = new AtomicInteger(10);

    public static void main(String[] args) {
        System.out.println(ai.getAndIncrement());
        System.out.println(ai.incrementAndGet());
        System.out.println(ai.get());
    }

}

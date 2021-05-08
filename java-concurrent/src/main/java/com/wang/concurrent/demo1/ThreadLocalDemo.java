package com.wang.concurrent.demo1;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * {@link ThreadLocal}
 * {@link InheritableThreadLocal}：子线程 可以访问 主线程 中的 ThreadLocal 对象，实现数据在父子线程间的传递。
 *
 * @description: ThreadLocal 的应用
 * @author: wei·man cui
 * @date: 2021/5/8 9:27
 */
public class ThreadLocalDemo {

    /**
     * {@link ThreadLocal}
     */
    public static void test() {
        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
        IntStream.range(0, 10).forEach(i -> {
            threadLocal.set(i);
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * {@link InheritableThreadLocal}
     */
    public static void test2() {
        ThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();
        IntStream.range(0, 10).forEach(i -> {
            threadLocal.set(i);
            try {
                TimeUnit.SECONDS.sleep(1);
                new Thread(() -> {
                    System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
                }).start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        // test();
        test2();
    }

}

package com.wang.concurrent.deadlock;

import com.wang.concurrent.demo1.SleepTools;

/**
 * 演示普通死锁和解决方法
 * 一个线程先拿第一个锁，再拿第二个锁；另一个线程先拿第二个锁，再拿第一个锁。
 *
 * @author weiman cui
 * @date 2020/5/17 17:12
 */
public class NormalDeadLock {

    /**
     * 第一个锁
     */
    private static Object valueFirst = new Object();
    /**
     * 第二个锁
     */
    private static Object valueSecond = new Object();

    /**
     * 先拿第一个锁，再拿第二个锁
     */
    private static void firstToSecond() {
        String threadName = Thread.currentThread().getName();
        synchronized (valueFirst) {
            System.out.println(threadName + " get First");
            SleepTools.ms(100);
            synchronized (valueSecond) {
                System.out.println(threadName + " get Second");
            }
        }
    }

    /**
     * 先拿第二个锁，再拿第一个锁
     */
    private static void secondToFirst() {
        String threadName = Thread.currentThread().getName();
        synchronized (valueSecond) {
            System.out.println(threadName + " get Second");
            SleepTools.ms(100);
            synchronized (valueFirst) {
                System.out.println(threadName + " get First");
            }
        }
    }

    private static class TestThread extends Thread {
        private String name;

        public TestThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            Thread.currentThread().setName(name);
            secondToFirst();
        }
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("MainThreadDeadLock");
        TestThread thread = new TestThread("SubTestThread");
        thread.start();
        firstToSecond();
    }

}

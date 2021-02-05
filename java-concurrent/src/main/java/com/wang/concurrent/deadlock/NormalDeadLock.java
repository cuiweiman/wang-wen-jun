package com.wang.concurrent.deadlock;

import com.wang.concurrent.demo1.SleepTools;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    /**
     * 死锁检测 线程
     */
    private static class DeadDetected implements Runnable {
        @Override
        public void run() {
            ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
            long[] threadIds = mxBean.findDeadlockedThreads();
            if (threadIds != null) {
                ThreadInfo[] threadInfos = mxBean.getThreadInfo(threadIds);
                System.out.println("---------- Detected deadLock threads：----------");
                for (ThreadInfo threadInfo : threadInfos) {
                    System.out.println(threadInfo.getThreadName());
                }
            }
        }
    }

    public static void main(String[] args) {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // 延时 0s 后开始执行，然后每 5s 执行一次
        scheduler.scheduleAtFixedRate(new DeadDetected(), 1L, 5L, TimeUnit.SECONDS);

        Thread.currentThread().setName("MainThreadDeadLock");
        TestThread thread = new TestThread("SubTestThread");
        thread.start();
        firstToSecond();
    }

}

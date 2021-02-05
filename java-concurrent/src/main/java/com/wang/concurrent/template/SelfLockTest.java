package com.wang.concurrent.template;

import com.wang.concurrent.demo1.SleepTools;

import java.util.concurrent.locks.Lock;

/**
 * 测试 自定义的 独占可重入锁
 */
public class SelfLockTest {

    public void test() {
        final Lock lock = new SelfLock();
        class Worker extends Thread {
            @Override
            public void run() {
                while (true) {
                    lock.lock();
                    try {
                        SleepTools.second(1);
                        System.out.println(Thread.currentThread().getName());
                        SleepTools.second(1);
                    } finally {
                        lock.unlock();
                    }
                    SleepTools.second(2);
                }
            }
        }

        //启动10个子线程
        for (int i = 0; i < 10; i++) {
            Worker w = new Worker();
            // 开启守护线程，主线程执行结束后，守护线程立即结束
            w.setDaemon(Boolean.TRUE);
            w.start();
        }

        // 主线程每个1s换行
        for (int i = 0; i < 10; i++) {
            SleepTools.second(1);
            System.out.println("i = " + i);
        }
    }

    public static void main(String[] args) {
        SelfLockTest selfLockTest = new SelfLockTest();
        selfLockTest.test();
    }

}

package com.wang.concurrent.classutils;

import com.wang.concurrent.demo1.SleepTools;

import java.util.concurrent.CountDownLatch;

/**
 * 线程倒计时器
 * 5个初始化线程，扣除计数为6。
 * 扣除完毕后，主线程和业务线程才能继续工作
 */
public class CountDownLatchDemo {

    static CountDownLatch latch = new CountDownLatch(6);

    // 初始化线程
    private static class InitThread implements Runnable {
        @Override
        public void run() {
            System.out.println("Thread_" + Thread.currentThread().getName() + " ready init work......");
            // 初始化工作完成，线程计数-1
            latch.countDown();
            for (int i = 0; i < 2; i++) {
                System.out.println("Thread_" + Thread.currentThread().getName() + " continue  work......");
            }
        }
    }

    // 业务线程：初始化完成后，才被执行
    private static class BussThread implements Runnable {
        @Override
        public void run() {
            try {
                latch.await();
                for (int i = 0; i < 3; i++) {
                    System.out.println("BussThread_" + Thread.currentThread().getName() + " do business");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 业务线程与main函数中，latch.await()方法之后的逻辑，需要在 CountDownLatch计数为0后才会继续执行。
     * 可以多处调用 latch.await()，一旦CountDownLatch计数为0，各处的 latch.await() 都会继续执行。
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        // 模拟 两步 前序 操作任务
        new Thread(() -> {
            SleepTools.ms(1);
            System.out.println("Thread_" + Thread.currentThread().getName() + " ready init work step 1st……");
            latch.countDown();

            System.out.println("begin step 2nd……");
            SleepTools.ms(1);
            System.out.println("Thread_" + Thread.currentThread().getName() + " ready init work step 2nd……");
            latch.countDown();
        }).start();

        // 启动 业务线程
        new Thread(new BussThread()).start();

        // 执行 四次 初始化线程,初始化线程中 计数器 减一，再算上main中模拟前序操作的步骤，计数6次。
        for (int i = 0; i < 4; i++) {
            Thread thread = new Thread(new InitThread());
            thread.start();
        }

        latch.await();
        System.out.println("Main do it's work……");
    }
}
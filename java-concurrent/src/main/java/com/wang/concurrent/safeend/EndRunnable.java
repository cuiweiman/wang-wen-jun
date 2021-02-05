package com.wang.concurrent.safeend;

/**
 * 安全的中断线程——Runnable
 */
public class EndRunnable {

    public static class UseRunnable implements Runnable {
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            // 判断 线程是否 中断，并返回当前 线程的 中断标志位
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(threadName + " is run!");
            }
            System.out.println(threadName + " interrupt flag is " + Thread.currentThread().isInterrupted());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        UseRunnable use = new UseRunnable();
        Thread thread = new Thread(use, "Runnable");
        thread.start();
        Thread.sleep(20);
        // 中断线程，将中断标志位 置为 TRUE
        thread.interrupt();
    }

}

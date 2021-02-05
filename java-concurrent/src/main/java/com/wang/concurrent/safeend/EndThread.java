package com.wang.concurrent.safeend;

/**
 * 安全的中断线程——Thread
 */
public class EndThread {

    public static class UseThread extends Thread {
        public UseThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            // 判断 线程是否 中断，并返回当前 线程的 中断标志位
            while (!isInterrupted()) {
                System.out.println(threadName + " is run!");
            }
            System.out.println(threadName + " interrupt flag is " + isInterrupted());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new UseThread("endThread");
        thread.start();
        Thread.sleep(1);
        // 中断线程，将中断标志位 置为 TRUE
        thread.interrupt();
    }

}

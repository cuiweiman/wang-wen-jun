package com.wang.concurrent.demo1;

/**
 * 开启 守护线程
 * 通过开启、关闭 守护线程，观察子线程的运行状态，从而了解守护线程的作用。
 */
public class DaemonThread {

    public static class UseThread extends Thread {
        public UseThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            try {
                String threadName = Thread.currentThread().getName();
                while (!isInterrupted()) {
                    System.out.println(threadName + " is run!");
                }
                System.out.println(threadName + " interrupt flag is " + isInterrupted());
            } finally {
                System.out.println("守护线程下，不一定会执行Finally方法");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new UseThread("【子线程】");
        // 开启/关闭 守护线程
        thread.setDaemon(Boolean.TRUE);
        thread.start();
        Thread.sleep(1);
        for (int i = 0; i < 100; i++) {
            System.out.println("main执行的次数：" + i);
        }
    }

}

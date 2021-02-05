package com.wang.concurrent.communition.waitnotify;

import java.util.concurrent.TimeUnit;

/**
 * 线程 通讯： wait、notify、notifyAll
 */
public class WaitAndNotify {
    public static void main(String[] args) {
        Object co = new Object();
        System.out.println(co);

        // 启动 5个线程，并进入 线程等待
        for (int i = 0; i < 5; i++) {
            MyThread t = new MyThread("Thread_" + i, co);
            t.start();
        }

        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("-----Main Thread notify-----");
            synchronized (co) {
                // 进行线程唤醒。唤醒一个/唤醒所有
                co.notify();
                // co.notifyAll();
            }
            TimeUnit.SECONDS.sleep(2);
            System.out.println("Main Thread is end.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class MyThread extends Thread {
        private String name;
        private Object co;

        public MyThread(String name, Object o) {
            this.name = name;
            this.co = o;
        }

        @Override
        public void run() {
            System.out.println(name + " is waiting.");
            try {
                synchronized (co) {
                    co.wait();
                }
                System.out.println(name + " has been notified.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

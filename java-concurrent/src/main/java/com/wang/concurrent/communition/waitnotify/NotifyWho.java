package com.wang.concurrent.communition.waitnotify;

import java.util.concurrent.TimeUnit;

/**
 * notify 究竟会按 wait的顺序 唤醒线程？还是 随机唤醒线程？
 * 经验证：随机唤醒  某一个 线程
 */
public class NotifyWho {

    public static void main(String[] args) {
        Object o = new Object();

        Thread t1 = new Thread(new MyThread("A", o));
        Thread t2 = new Thread(new MyThread("B", o));
        Thread t3 = new Thread(new MyThread("C", o));
        Thread t4 = new Thread(new MyThread("D", o));
        Thread t5 = new Thread(new MyThread("E", o));
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        try {
            TimeUnit.SECONDS.sleep(3);
            synchronized (o) {
                System.out.println("==================");
                o.notify();
                o.notify();
                o.notify();
                o.notify();
                o.notify();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static class MyThread extends Thread {
        private String name;
        private Object o;

        public MyThread(String name, Object o) {
            this.name = name;
            this.o = o;
        }

        @Override
        public void run() {
            System.out.println("Thread_" + name + " is waiting");
            try {
                synchronized (o) {
                    o.wait();
                }
                System.out.println("Thread_" + name + " has been notified");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}

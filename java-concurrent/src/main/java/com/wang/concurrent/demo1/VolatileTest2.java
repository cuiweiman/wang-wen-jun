package com.wang.concurrent.demo1;

/**
 * volatile 实现 线程间变量的可见性
 * 发现: 有了 System.out.println(Thread.currentThread().getName() + " " + flag);或者 Thread.sleep(); 之后，不论是否 volatile修饰，都会结束。
 * 难道： println 可以实现 可见性？
 * 原因： println 方法是被synchronized修饰的。
 */
public class VolatileTest2 {

    // private static boolean flag = true;
    private static volatile boolean flag = true;

    static class Monitor implements Runnable {
        @Override
        public void run() {
            while (flag) {
                // System.out.println(Thread.currentThread().getName() + " " + flag);
            }
            System.out.println(Thread.currentThread().getName() + " " + flag);
        }
    }

    static class Change implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                flag = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Thread monitor = new Thread(new Monitor(), "【monitor】");
        Thread change = new Thread(new Change(), "【change】");

        monitor.start();
        change.start();
    }


    /*public static void main(String[] args) {
        new Thread() {
            int i = 0;

            public void run() {
                long tm = System.currentTimeMillis();
                while (flag) {
                    i++;
                    // System.out.println(++i);
                }
                System.out.println(System.currentTimeMillis() - tm + " ms");
            }
        }.start();

        new Thread(() -> {
            try {
                Thread.sleep(200);
                flag = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }*/

}
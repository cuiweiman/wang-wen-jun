package com.wang.basic.thread.basic;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2020/10/28 17:26
 */
public class RunnableDemo implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " ==> " + i);
        }
    }
    public static void main(String[] args) {
        Thread thread = new Thread(new RunnableDemo(), "【线程二】");
        thread.start();
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " ==> " + i);
        }
    }
}

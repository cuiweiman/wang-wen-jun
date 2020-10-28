package com.wang.basic.thread.basic;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2020/10/28 17:23
 */
public class ThreadDemo1 extends Thread {
    public ThreadDemo1(String name) {
        super(name);
    }
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " ==> " + i);
        }
    }
    public static void main(String[] args) {
        ThreadDemo1 demo1 = new ThreadDemo1("【线程二】");
        demo1.start();
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " ==> " + i);
        }
    }
}

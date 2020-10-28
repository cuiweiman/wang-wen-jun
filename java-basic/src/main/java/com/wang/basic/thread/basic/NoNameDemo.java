package com.wang.basic.thread.basic;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2020/10/28 17:34
 */
public class NoNameDemo {

    public static void main(String[] args) {
        new Thread(()->{
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + " ==> " + i);
            }
        },"【任务二】").start();
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " ==> " + i);
        }
    }

}

package com.wang.basic.thread.communication;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 线程通信
 * @author: wei·man cui
 * @date: 2020/10/28 18:24
 */
public class ThreadCommunication {


    public static void main(String[] args) {
        Object obj = new Object();
        new Thread(() -> {
            while (true) {
                synchronized (obj) {
                    System.out.println("包子的种类");
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("那我开始吃包子咯");
                    System.out.println("===========下一位顾客=============");
                }
            }
        }, "顾客").start();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj) {
                    System.out.println("包子做好了");
                    obj.notify();
                }
            }
        }, "老板").start();


    }

}

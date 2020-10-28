package com.wang.basic.threadpool;

/**
 * @description:
 * @date: 2020/10/28 20:55
 * @author: wei·man cui
 */
public class RunnableTask implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 创建了新的线程");
    }
}

package com.wang.basic.thread.basic;

/**
 * 实现一个多线程类，并用该线程类实例化3个线程A,B,C；
 * A线程打印字符A,B线程打印字符B，C线程打印字符C；
 * 启动这3个线程，要求启动线程的顺序为C线程->B线程->A线程，并且最后输出内容为：
 *
 * @description: Thread.Join的使用
 * @author: wei·man cui
 * @date: 2021/1/21 9:33
 */
@SuppressWarnings("all")
public class ThreadJoinDemo {

    public static void main(String[] args) {

        final Thread threadA = new Thread(() -> {
            System.out.println("A");
        });
        final Thread threadB = new Thread(() -> {
            try {
                threadA.join();
                System.out.println("B");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        final Thread threadC = new Thread(() -> {
            try {
                threadB.join();
                System.out.println("C");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadC.start();
        threadB.start();
        threadA.start();
    }
}

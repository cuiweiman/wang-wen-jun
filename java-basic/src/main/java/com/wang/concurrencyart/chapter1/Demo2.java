package com.wang.concurrencyart.chapter1;

import sun.awt.util.IdentityArrayList;

/**
 * @description: 死锁
 * @date: 2021/2/1 23:16
 * @author: wei·man cui
 */
public class Demo2 {

    private static String A = "A";
    private static String B = "B";

    /**
     * jps -l： 查看进程 PID
     * jstack pid：查看线程状况
     * @param args 参数
     */
    public static void main(String[] args) {
        deadLock();
    }

    private static void deadLock() {
        new Thread(() -> {
            synchronized (A) {
                try {
                    Thread.currentThread().sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B){
                    System.out.println("获取 B 锁");
                }
            }
        }, "[线程 1]").start();

        new Thread(() -> {
            synchronized (B) {
                try {
                    Thread.currentThread().sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (A){
                    System.out.println("获取 A 锁");
                }
            }
        }, "[线程 2]").start();
    }

}

package com.wang.concurrent.classutils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Exchanger;

/**
 * Exchange 两个线程间的数据交换
 * 当两个线程执行了 Exchanger.exchange();方法后，会交换两个线程的数据
 * 首先运行到 exchange();的线程，会进入阻塞等待另一个线程。
 */
public class ExchangeDemo {
    private static final Exchanger<Set<String>> exchange = new Exchanger<>();

    public static void main(String[] args) {
        // 启动 第一个线程
        new Thread(() -> {
            //存放数据的容器
            Set<String> setA = new HashSet<>();
            try {
                setA.add("Thread_A_1");
                setA.add("Thread_A_2");
                setA.add("Thread_A_3");
                // 交换 set 容器
                setA = exchange.exchange(setA);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("【setA】" + setA.toString());
        }).start();

        // 启动第二个线程
        new Thread(() -> {
            // 存放数据的容器
            Set<String> setB = new HashSet<>();
            try {
                // 交换 set 容器
                setB = exchange.exchange(setB);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("【setB】" + setB.toString());
        }).start();

    }


}

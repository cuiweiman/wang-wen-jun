package com.wang.basic.thread.threadsync;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 买票问题
 * @author: wei·man cui
 * @date: 2020/10/28 17:38
 */
public class ThreadSyncTicket implements Runnable {
    private int ticket = 100;

    Object obj = new Object();

    Lock l = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            l.lock();
            if (ticket > 0) {
                ticket--;
                System.out.println(Thread.currentThread().getName() + "卖了一张票，还剩余：" + ticket);
            }
            l.unlock();
        }
    }

    /*@Override
    public void run() {
        while (true) {
            synchronized (obj) {
                if (ticket > 0) {
                    ticket--;
                    System.out.println(Thread.currentThread().getName() + "卖了一张票，还剩余：" + ticket);
                }
            }
        }
    }*/

    // 买票 不正确
    /*@Override
    public void run() {
        while (true) {
            if (ticket > 0) {
                ticket--;
                System.out.println(Thread.currentThread().getName() + "卖了一张票，还剩余：" + ticket);
            }
        }
    }*/

    public static void main(String[] args) {
        ThreadSyncTicket runnable = new ThreadSyncTicket();
        Thread t1 = new Thread(runnable, "【窗口A】");
        t1.start();
        Thread t2 = new Thread(runnable, "【窗口B】");
        t2.start();
        Thread t3 = new Thread(runnable, "【窗口C】");
        t3.start();
        // test();
    }


    public static void test() {
        int[] arr = {99, 96, 95, 97, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83, 82, 81, 80, 98, 79, 94, 77, 75,
                74, 73, 72, 71, 70, 69, 68, 67, 66, 65, 78, 64, 62, 61, 60, 59, 58, 57, 56, 55, 54, 53, 52, 76, 50,
                49, 48, 47, 51, 63, 45, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25,
                24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 46, 14, 12, 44, 10, 9, 8, 7, 6, 5, 4, 3, 2, 11, 13, 0, 1};
        for (int step = arr.length / 2; step > 0; step /= 2) {
            //遍历 根据步长分组的 数组
            for (int i = step; i < arr.length; i++) {
                //根据步长进行分组，分组内类似插入排序
                for (int j = i - step; j >= 0; j -= step) {
                    if (arr[j] > arr[j + step]) {
                        int temp = arr[j];
                        arr[j] = arr[j + step];
                        arr[j + step] = temp;
                    }
                    System.out.println("step=" + step + "，i=" + i + "，j=" + j + "，" + Arrays.toString(arr));
                }
            }
        }
    }


}

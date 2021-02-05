package com.wang.concurrent.demo1;

/**
 * Synchronized JDK内置锁
 * 修饰方法、修饰同步代码块；同一时刻，只有一个线程可以执行某个方法或代码块。
 */
public class SynchronizedTest implements Runnable {
    public static Integer count = 0;

    // 修饰代码块
    /*@Override
    public void run() {
        synchronized (this) {
            for (int i = 0; i < 10; i++) {
                count++;
                System.out.println(Thread.currentThread().getName() + " count = " + count);
                SleepTools.ms(100);
            }
        }
    }*/

    //修饰方法
    public synchronized void increase() {
        count++;
        System.out.println(Thread.currentThread().getName() + " count = " + count);
        SleepTools.ms(100);
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            increase();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedTest runAble = new SynchronizedTest();
        Thread blockThread1 = new Thread(runAble, "【BLOCK1】");
        Thread blockThread2 = new Thread(runAble, "【BLOCK2】");
        blockThread1.start();
        blockThread2.start();
    }


}

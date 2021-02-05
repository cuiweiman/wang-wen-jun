package com.wang.concurrent.demo1;

/**
 * 当两个并发线程(t1和t2)访问同一个对象(SynchronizedTest2)中的synchronized代码块时，
 * 在同一时刻只能有一个线程得到执行，另一个线程受阻塞，
 * 必须等待当前线程执行完这个代码块以后才能执行该代码块。
 * t1和t2是互斥的，因为在执行synchronized代码块时会锁定当前的对象，
 * 只有执行完该代码块才能释放该对象锁，下一个线程才能执行并锁定该对象
 */
public class SynchronizedTest2 implements Runnable {

    private static int count;

    public SynchronizedTest2() {
        count = 0;
    }

    @Override
    public void run() {
        synchronized (this) {
            for (int i = 0; i < 5; i++) {
                try {
                    count++;
                    System.out.println(Thread.currentThread().getName() + "  " + count);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SynchronizedTest2 test2 = new SynchronizedTest2();
        Thread t1 = new Thread(test2, "【thread_I】");
        Thread t2 = new Thread(test2, "【thread_II】");
        t1.start();
        t2.start();
    }

}

package com.wang.concurrent.demo1;

/**
 * volatile 无法保证线程安全。不具有原子性。
 * 示例如下：a=a+1 或者 a++ 都不是原子操作。
 */
public class VolatileTest {

    private static class VolatileVar implements Runnable {
        private volatile int a = 0;

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            a = a + 1;
            System.out.println(name + " a=" + a);
            SleepTools.ms(100);
            a = a + 1;
            System.out.println(name + " a=" + a);
        }
    }

    public static void main(String[] args) {
        VolatileVar v = new VolatileVar();
        Thread t1 = new Thread(v, "【A】");
        Thread t2 = new Thread(v, "【B】");
        Thread t3 = new Thread(v, "【C】");
        Thread t4 = new Thread(v, "【D】");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

}

package com.wang.java8.completablefuture;

import java.util.Random;

/**
 * @description: Thread.join()方法测试
 * @author: weiman cui
 * @date: 2020/7/1 17:49
 */
public class JoinTest extends Thread {

    public JoinTest(String name) {
        super(name);
    }

    @Override
    public void run() {
        Random random = new Random(System.currentTimeMillis());
        try {
            Thread.sleep(random.nextInt(5000));
            System.out.println(Thread.currentThread().getName() + "  Finished.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        JoinTest t1 = new JoinTest("线程-1");
        JoinTest t2 = new JoinTest("线程-2");
        JoinTest t3 = new JoinTest("线程-3");
        JoinTest t4 = new JoinTest("线程-4");
        JoinTest t5 = new JoinTest("线程-5");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // 需要等 t4 执行结束后，才继续向下执行
        t4.join();

        t5.start();

    }
}

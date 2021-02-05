package com.wang.concurrent.threadpool;

import lombok.Getter;

import java.util.Random;

/**
 * 自定义线程池测试
 */
public class TestMyThreadPool {

    public static void main(String[] args) throws InterruptedException {
        MyThreadPool pool = new MyThreadPool(3, 0);
        pool.execute(new MyTask("testA"));
        pool.execute(new MyTask("testB"));
        pool.execute(new MyTask("testC"));
        pool.execute(new MyTask("testD"));
        pool.execute(new MyTask("testE"));
        System.out.println(pool);
        Thread.sleep(1000);
        pool.destroy();
        System.out.println(pool);
    }

    @Getter
    static class MyTask implements Runnable {
        private String name;
        private Random r = new Random();

        public MyTask(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(r.nextInt(1000) + 2000);
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getId()
                        + " sleep Interrupt "
                        + Thread.currentThread().isInterrupted());
            }
            System.out.println("任务 " + name + " 完成");
        }
    }

}

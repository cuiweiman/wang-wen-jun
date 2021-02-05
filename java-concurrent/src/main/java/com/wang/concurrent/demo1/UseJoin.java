package com.wang.concurrent.demo1;

/**
 * join()方法的使用案例
 * join函数可以使线程按照顺序执行，
 */
public class UseJoin {

    // 线程 插队 执行 （B的运行程序中，调用A.join()）
    static class JumpQueue implements Runnable {
        private Thread thread;

        public JumpQueue(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " terminated.");
        }
    }

    public static void main(String[] args) {
        // 初始 是主线程main
        Thread previous = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new JumpQueue(previous), String.valueOf(i));
            System.out.println(previous.getName() + " jump a queue. Thread: " + thread.getName());
            thread.start();
            previous = thread;
        }
        SleepTools.second(2);
        System.out.println(Thread.currentThread().getName() + " terminated.");
    }

}

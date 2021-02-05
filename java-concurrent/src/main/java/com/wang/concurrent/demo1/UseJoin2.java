package com.wang.concurrent.demo1;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class UseJoin2 {

    public static class UseThread extends Thread {
        public UseThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
        }
    }

    public static class UseRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
        }
    }

    public static class UseCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println(Thread.currentThread().getName());
            return "Success";
        }
    }

    // 按照 B线程 ——> main ——> C线程 ——> A线程 的顺序执行
    public static void main(String[] args) throws InterruptedException {
        UseThread thread = new UseThread("A线程");
        Thread runnable = new Thread(new UseRunnable(), "B线程");
        Thread callable = new Thread(new FutureTask<>(new UseCallable()), "C线程");
        FutureTask<String> task = new FutureTask<>(new UseCallable());
        Thread future = new Thread(task, "D线程");

        // A线程必须 在 B、C、D、main之前执行
        thread.start();
        thread.join();

        runnable.start();
        callable.start();
        future.start();
        System.out.println(Thread.currentThread().getName());


        /*runnable.start();
        runnable.join();

        System.out.println(Thread.currentThread().getName());

        callable.start();
        callable.join();

        thread.start();
        thread.join();

        FutureTask<String> task = new FutureTask<>(new UseCallable());
        Thread callThread = new Thread(task, "D线程");
        callThread.start();*/
    }
}

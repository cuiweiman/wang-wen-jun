package com.wang.concurrent.demo1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 使用 Thread类、Runnable接口、Callable接口
 * 分别实现 多线程
 */
public class NewThread {

    private static class UseThread extends Thread {
        public UseThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println("继承 Thread 类");
        }
    }

    private static class UseRun implements Runnable {
        @Override
        public void run() {
            System.out.println("Runnable 不处理异常，没有返回值");
        }
    }

    private static class UseCall implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("Callable 可以有异常，有返回值");
            return "CallResult";
        }
    }

    public static void main(String[] args) {
        UseThread useThread = new UseThread("【Thread】");
        useThread.start();

        UseRun useRun = new UseRun();
        new Thread(useRun).start();

        try {
            UseCall useCall = new UseCall();
            FutureTask<String> futureTask = new FutureTask<>(useCall);
            new Thread(futureTask).start();
            System.out.println(futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}

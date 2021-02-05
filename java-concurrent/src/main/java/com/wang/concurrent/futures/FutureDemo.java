package com.wang.concurrent.futures;

import com.wang.concurrent.demo1.SleepTools;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Future的使用
 */
public class FutureDemo {

    private static class UseCallable implements Callable<Integer> {
        private int sum;

        @Override
        public Integer call() throws Exception {
            System.out.println("Callable 子线程开始计算");
            Thread.sleep(2000);
            for (int i = 0; i < 5000; i++) {
                sum += i;
            }
            System.out.println("Callable 子线程计算结束。结果为：sum=" + sum);
            return sum;
        }
    }

    /**
     * 多次执行本方法，发现 子线程会运行有两种情况：运行结束、被中断。
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        UseCallable useCallable = new UseCallable();
        FutureTask<Integer> task = new FutureTask<>(useCallable);
        new Thread(task).start();

        SleepTools.second(1);

        // 随机决定，是获取结果，还是终止任务
        Random r = new Random();
        if (r.nextBoolean()) {
            System.out.println("获取子线程运行结果 result=" + task.get());
        } else {
            System.out.println("中断子线程的计数");
            task.cancel(Boolean.TRUE);
        }
        System.out.println("执行结束：" + task.isDone() + "; 任务是否被取消执行：" + task.isCancelled());
    }

}

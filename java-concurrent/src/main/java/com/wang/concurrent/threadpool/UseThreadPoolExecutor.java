package com.wang.concurrent.threadpool;

import com.wang.concurrent.demo1.SleepTools;

import java.util.Random;
import java.util.concurrent.*;

/**
 * JDK线程池——ThreadPoolExecutor的使用
 */
public class UseThreadPoolExecutor {
    static class Worker implements Runnable {
        private String taskName;
        private Random r = new Random();

        public Worker(String taskName) {
            this.taskName = taskName;
        }

        public String getTaskName() {
            return taskName;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()
                    + " process the task: " + taskName);
            SleepTools.ms(r.nextInt(1000) * 5);
        }
    }

    static class CallWorker implements Callable<String> {
        private String taskName;
        private Random r = new Random();

        public CallWorker(String taskName) {
            this.taskName = taskName;
        }

        public String getTaskName() {
            return taskName;
        }

        @Override
        public String call() throws Exception {
            System.out.println(Thread.currentThread().getName()
                    + " process the task: " + taskName);
            SleepTools.ms(r.nextInt(1000) * 5);
            return taskName;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int corePoolSize = 2;
        int maximumPoolSize = 4;
        long keepAliveTime = 3;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardOldestPolicy();

        ExecutorService pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue, handler);

        for (int i = 0; i < 6; i++) {
            Worker worker = new Worker("worker__" + i);
            // 无返回值
            pool.execute(worker);
        }
        for (int i = 0; i < 6; i++) {
            CallWorker callWorker = new CallWorker("call__worker__" + i);
            // 有返回值
            Future<String> result = pool.submit(callWorker);
            System.out.println(result.get());
        }
        pool.shutdown();
    }

}

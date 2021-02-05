package com.wang.concurrent.threadpool;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 自定义 线程池 的实现
 */
public class MyThreadPool {

    // 默认线程个数
    private static int WORK_NUM = 5;

    // 阻塞队列 中 默认任务个数
    private static int TASK_COUNT = 100;

    // 工作线程
    private WorkThread[] workThreads;

    // 创建 具有默认线程个数 的线程池,存放 未获取到线程资源的请求
    private final BlockingQueue<Runnable> taskQueue;
    private final int worker_num;

    public MyThreadPool() {
        this(WORK_NUM, TASK_COUNT);
    }

    // 创建线程池，worker_num 为线程池中工作线程个数
    public MyThreadPool(int worker_num, int taskCount) {
        // 若参数 为0，则将默认参数赋值给它们
        if (worker_num <= 0)
            worker_num = WORK_NUM;
        if (taskCount <= 0)
            taskCount = TASK_COUNT;

        this.worker_num = worker_num;
        taskQueue = new ArrayBlockingQueue<>(taskCount);

        workThreads = new WorkThread[worker_num];
        for (int i = 0; i < worker_num; i++) {
            workThreads[i] = new WorkThread();
            workThreads[i].start();
        }

    }


    // 执行任务，其实只是将任务加入任务队列，什么时候执行，由线程池管理器决定
    public void execute(Runnable task) {
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 销毁线程池，该方法保证在所有任务都完成的情况下才销毁所有线程，否则等待任务完成才销毁
    public void destroy() {
        // 工作线程停止工作，且置为null
        System.out.println("ready close pool……");
        for (int i = 0; i < worker_num; i++) {
            workThreads[i].stopWorker();
            // 帮助GC
            workThreads[i] = null;
        }
        // 清空任务队列
        taskQueue.clear();
    }

    @Override
    public String toString() {
        return "WorkThread number： " + worker_num
                + "；wait task number：" + taskQueue.size();
    }

    /**
     * 内部类，工作线程
     * 从阻塞队列中，获取相应的线程任务，并执行
     */
    private class WorkThread extends Thread {
        @Override
        public void run() {
            Runnable r = null;
            try {
                while (!isInterrupted()) {
                    r = taskQueue.take();
                    if (r != null) {
                        System.out.println(getId() + " ready execute。");
                        r.run();
                    }
                    // 帮助GC
                    r = null;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 中断 当前 执行的 线程任务
        public void stopWorker() {
            interrupt();
        }

    }
}

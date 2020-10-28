package com.wang.basic.threadpool;

import java.util.concurrent.*;

/**
 * @description: 推荐 手动 创建线程池
 * @date: 2020/10/28 21:02
 * @author: wei·man cui
 */
public class ThreadPoolConfig {

    /**
     * 核心线程数 = CPU线程数，线程池中一直存在
     */
    // private static Integer corePoolSize = Runtime.getRuntime().availableProcessors();
    private static Integer corePoolSize = 2;

    /**
     * 线程池中允许存在的最大线程数。
     * 当核心线程用完，任务队列BlockingQueue堆满后，才允许创建 临时线程 执行任务，
     * 临时线程 会 执行 阻塞队列中等待着的任务。
     * 临时线程 空闲后，达到最大存活时间 keepLiveTime 后会被销毁。
     */
    private static Integer maximumPoolSize = corePoolSize * 2;
    private static Long keepLiveTime = 5L;
    private static TimeUnit unit = TimeUnit.SECONDS;

    /**
     * 核心线程用完后，任务存储在阻塞队列中，等待被执行
     */
    private static BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>(maximumPoolSize);

    /**
     * 设置线程名 前缀
     */
    private String threadNamePrefix = "BusinessThreadPool-";

    /**
     * 任务拒绝策略。
     * 当核心线程用完、阻塞队列堆满、线程池中线程数达到最大时，拒绝任务的策略。
     * ThreadPoolExecutor.AbortPolicy()：默认策略，丢弃任务并抛出异常
     * ThreadPoolExecutor.DiscardPolicy()：丢弃任务，不抛出异常
     * ThreadPoolExecutor.CallerRunsPolicy()：提交该任务的线程，去执行这个任务
     * ThreadPoolExecutor.DiscardOldestPolicy()：抛弃等待最久的任务（最先进入阻塞队列但未得到执行的任务）
     */
    private static RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();

    public static ThreadPoolExecutor asyncExecutor() {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepLiveTime, unit,
                workQueue, Executors.defaultThreadFactory(), handler);
    }

}

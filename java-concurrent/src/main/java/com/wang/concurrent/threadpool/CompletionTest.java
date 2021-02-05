package com.wang.concurrent.threadpool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通过 自己手写逻辑来获取线程池中任务的返回结果，对比 使用 CompletionService 服务
 * 来了解 CompletionService 的作用和功能
 * (先执行结束的线程，先拿到其结果)
 */
public class CompletionTest {

    private final int POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private final int TOTAL_TASK = Runtime.getRuntime().availableProcessors();

    /**
     * 方法一，自己写集合来实现 获取线程池中任务的返回结果
     *
     * @throws Exception
     */
    public void testByQueue() throws Exception {
        long start = System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger(0);

        //创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        // 阻塞队列 中 存放 向线程池中提交的任务
        BlockingQueue<Future<Integer>> queue = new LinkedBlockingDeque<>();

        // 向线程池 提交任务
        for (int i = 0; i < TOTAL_TASK; i++) {
            Future<Integer> future = pool.submit(new CompletionWorker("ExecTask__" + i));
            // 按照i的顺序，添加到队列中。在取出时，也是按照i的顺序。按顺序FIFO。
            queue.add(future);
        }

        // 检查线程池任务执行结果
        for (int i = 0; i < TOTAL_TASK; i++) {
            // 从 阻塞队列中 获取 提交的任务
            int sleepTime = queue.take().get();
            System.out.println("slept " + sleepTime + " ms……");
            count.addAndGet(sleepTime);
        }

        // 关闭线程池
        pool.shutdown();
        System.out.println("========tasks sleep tme " + count.get()
                + "ms, and spend time "
                + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     * 方法二：通过 CompletionService 来实现获取线程池中任务的返回结果
     *
     * @throws Exception
     */
    public void testByCompletion() throws Exception {
        long start = System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger(0);
        // 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        // 使用 CompletionService 接收 线程返回值。线程先执行结束的先返回，没有顺序的限制。
        // cService 这里封装了 线程池，提交任务直接使用 cService 提交。
        CompletionService<Integer> cService = new ExecutorCompletionService<>(pool);

        for (int i = 0; i < TOTAL_TASK; i++) {
            cService.submit(new CompletionWorker("ExecTask__" + i));
            // 不需要 特意向 CompletionService中添加 任务
        }

        // 检查线程池任务执行结果
        for (int i = 0; i < TOTAL_TASK; i++) {
            // 从 阻塞队列中 获取 提交的任务
            int sleepTime = cService.take().get();
            System.out.println("slept " + sleepTime + " ms……");
            count.addAndGet(sleepTime);
        }

        // 关闭线程池
        pool.shutdown();
        System.out.println("========tasks sleep tme " + count.get()
                + "ms, and spend time "
                + (System.currentTimeMillis() - start) + " ms");
    }

    public static void main(String[] args) throws Exception {
        CompletionTest t = new CompletionTest();
        t.testByQueue();
        t.testByCompletion();
    }

}

package com.wang.advanced.executor.concurrent;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * @description: 线程池服务对象
 * @author: wei·man cui
 * @date: 2021/3/11 18:21
 */
public class ExecutorServiceObject {

    private final ThreadPoolExecutor threadPoolExecutor;

    private final BlockingQueue<Runnable> workQueue;


    /**
     * 创建线程池服务对象
     *
     * <p> https://www.cnblogs.com/koushr/p/11756031.html
     * <p> https://blog.csdn.net/weixin_37669199/article/details/105280142
     *
     * <p> {@link MoreExecutors#getExitingExecutorService} 将 ThreadPoolExecutor 实例转换成：
     * 应用结束后 自动退出的 ExecutorService 实例。在用户线程执行结束后，JVM 会至多等待一定时间（默认120s），使得线程池线程执行结束。
     *
     * <P>{@link MoreExecutors#listeningDecorator}
     *
     * @return 线程池服务对象
     */
    public ExecutorService createExecutorService() {
        final ExecutorService executorService = MoreExecutors.getExitingExecutorService(this.threadPoolExecutor);
        return MoreExecutors.listeningDecorator(executorService);
    }


    public ExecutorServiceObject(final String threadName, final int threadSize, final boolean daemon) {
        this.workQueue = new LinkedBlockingQueue<>();
        this.threadPoolExecutor = new ThreadPoolExecutor(threadSize, threadSize, 5L, TimeUnit.MINUTES, workQueue,
                new BasicThreadFactory.Builder().namingPattern(Joiner.on("-").join(threadName, "%s")).daemon(daemon).build());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
    }

    /**
     * 线程池是否关闭
     *
     * @return 结果
     */
    public boolean isShutdown() {
        return threadPoolExecutor.isShutdown();
    }

    /**
     * 获取当前活跃的线程数.
     *
     * @return 当前活跃的线程数
     */
    public int getActiveThreadCount() {
        return this.threadPoolExecutor.getActiveCount();
    }

    /**
     * 获取待执行任务数量.
     *
     * @return 待执行任务数量
     */
    public int getWorkQueueSize() {
        return this.workQueue.size();
    }

}

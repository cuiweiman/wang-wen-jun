package com.wang.advanced.executor;

import java.util.concurrent.ExecutorService;

/**
 * @description: 线程池服务处理器
 * @author: wei·man cui
 * @date: 2021/3/11 18:20
 */
public interface ExecutorServiceHandler {

    /**
     * 创建线程池服务对象
     *
     * @param threadName 线程名
     * @param daemon     是否守护线程
     * @return 线程
     */
    ExecutorService createExecutorService(final String threadName, final boolean daemon);


}

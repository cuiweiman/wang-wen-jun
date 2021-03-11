package com.wang.advanced.executor;

import com.wang.advanced.executor.concurrent.ExecutorServiceObject;

import java.util.concurrent.ExecutorService;

/**
 * @description: 默认线程池服务处理器：现在是，每使用一次都会创建一个线程池。构造成单例？
 * @author: wei·man cui
 * @date: 2021/3/11 18:21
 */
public class DefaultExecutorHandler implements ExecutorServiceHandler {

    @Override
    public ExecutorService createExecutorService(String threadName, boolean daemon) {
        return new ExecutorServiceObject(threadName, Runtime.getRuntime().availableProcessors() * 2 + 1, daemon)
                .createExecutorService();
    }
}

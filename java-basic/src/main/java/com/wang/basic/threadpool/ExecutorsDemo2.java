package com.wang.basic.threadpool;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: JDK 线程池
 * @date: 2020/10/28 20:49
 * @author: wei·man cui
 */
public class ExecutorsDemo2 {

    public static void main(String[] args) {

        ThreadPoolExecutor executor = ThreadPoolConfig.asyncExecutor();

        for (int i = 0; i < 20; i++) {
            executor.execute(new RunnableTask());
        }
        executor.shutdown();
    }

}

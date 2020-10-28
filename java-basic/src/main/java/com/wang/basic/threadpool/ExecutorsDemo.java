package com.wang.basic.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: JDK 线程池
 * @date: 2020/10/28 20:49
 * @author: wei·man cui
 */
public class ExecutorsDemo {

    private final static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            executor.submit(new RunnableTask());
        }
        executor.shutdown();
    }

}

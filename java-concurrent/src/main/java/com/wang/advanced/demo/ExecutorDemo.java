package com.wang.advanced.demo;

import com.wang.advanced.executor.DefaultExecutorHandler;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 使用
 * @author: wei·man cui
 * @date: 2021/3/11 20:10
 */
public class ExecutorDemo {

    static ExecutorService executorService = new DefaultExecutorHandler().createExecutorService("test", false);

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        runAsync();
    }

    public static void runAsync() throws InterruptedException, ExecutionException, TimeoutException {
        AtomicInteger count = new AtomicInteger(0);
        final CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            for (int i = 1; i <= 100; i++) {
                count.getAndAdd(i);
            }
            return count.get();
        }, executorService).exceptionally(e -> {
            // 获取异常
            e.printStackTrace();
            return null;
        });
        System.out.println(future.get(3, TimeUnit.SECONDS));
    }

}

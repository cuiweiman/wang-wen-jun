package com.wang.basic.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @description: JDK 线程池
 * @date: 2020/10/28 20:49
 * @author: wei·man cui
 */
public class ExecutorsDemo2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ThreadPoolExecutor executor = ThreadPoolConfig.asyncExecutor();
        List<Future<String>> futures = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            final int x = i;
            final Future<String> submit = executor.submit(() -> doZip(x));
            futures.add(submit);
        }
        for (Future<String> future : futures) {
            System.out.println(future.get());
        }
        System.out.println("Finish");
        // 实际项目中，维护的线程池不需要关闭
        executor.shutdown();
    }

    public static String doZip(int i) {
        try {
            System.out.println("begin do zip......." + i);
            TimeUnit.SECONDS.sleep(1);
            System.out.println("done______" + i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "test.zip";
    }
}

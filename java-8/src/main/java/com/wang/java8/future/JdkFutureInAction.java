package com.wang.java8.future;

import java.util.concurrent.*;

/**
 * @description: JDK自带的Future
 * @author: weiman cui
 * @date: 2020/7/1 11:29
 */
public class JdkFutureInAction {
    public static void main(String[] args) {
        // 推荐手动 ThreadPoolExecutor 创建线程池
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> {
            try {
                Thread.sleep(5000L);
                return "I am finished.";
            } catch (InterruptedException e) {
                return "I am error.";
            }
        });

        while (!future.isDone()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            // 若还没有执行结束，会阻塞，直到获取到值
            String value = future.get(3, TimeUnit.MICROSECONDS);
            System.out.println(value);
            // 需要手动关闭线程池
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("超时");
        }


    }


}

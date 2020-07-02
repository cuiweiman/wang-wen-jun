package com.wang.java8.completablefuture;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @description: CompletableFuture 简单示例
 * @author: weiman cui
 * @date: 2020/7/1 13:43
 */
public class CompletableFuture1 {

    private final static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Double> completableFuture = new CompletableFuture();
        new Thread(() -> {
            double value = get();
            completableFuture.complete(value);
        }).start();
        System.out.println("=== no block ===");
        // 任务执行结束后，然后执行
        completableFuture.whenComplete((v, t) -> {
            Optional.ofNullable(v).ifPresent(System.out::println);
            Optional.ofNullable(t).ifPresent(x -> x.printStackTrace());
        });
    }

    protected static double get() {
        try {
            Thread.sleep(random.nextInt(5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Double result = random.nextDouble();
        System.out.println(result);
        return result;
    }

}

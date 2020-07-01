package com.wang.java8.completablefuture;

import java.util.concurrent.CompletableFuture;

/**
 * @description: CompletableFuture 常用API
 * @date: 2020/7/1 22:36
 * @author: weiman cui
 */
public class CompletableFuture4 {

    public static void main(String[] args) throws InterruptedException {

        /**
         * thenApply: 返回CompletableFuture，但是会转变泛型的类型
         */
        CompletableFuture.supplyAsync(() -> 1)
                .thenApply(i -> Integer.sum(i, 10))
                .whenComplete((v, t) -> System.out.println(v));

        /**
         *  handle() 可以处理异常，其它的与thenApply()无异
         */
        CompletableFuture.supplyAsync(() -> 1)
                .handle((v, t) -> Integer.sum(v, 10))
                .whenComplete((v, t) -> System.out.println(v));

        /**
         * thenAccept() 消费性函数，使用结果
         */
        CompletableFuture.supplyAsync(() -> 1).thenApply(i -> Integer.sum(i, 9))
                .thenAccept(System.out::println);

        /**
         * thenCompose() 组合设计模式：将supplyAsync的结果 再交给 CompletableFuture。
         * 在异步操作完成的时候对异步操作的结果进行一些操作，并且仍然返回CompletableFuture类型
         */
        CompletableFuture.supplyAsync(() -> 1)
                .thenCompose(i -> CompletableFuture.supplyAsync(() -> 10 * i))
                .thenAccept(System.out::println);

        /**
         * thenCombine
         */
        CompletableFuture.supplyAsync(() -> 1)
                .thenCombine(CompletableFuture.supplyAsync(() -> 2.0d), (r1, r2) -> r1 + r2)
                .thenAccept(System.out::println);


        /**
         * thenAcceptBoth
         */
        CompletableFuture.supplyAsync(() -> 1)
                .thenAcceptBoth(CompletableFuture.supplyAsync(() -> 2.0d), (r1, r2) -> System.out.println(r1 + "  " + r2));

        /**
         * <p>
         *     CompletableFuture使用的是守护线程，
         *     主线程结束后不等其执行完毕，直接异步执行结束
         *     守护线程(子线程)就会随之结束，所以在此休眠
         * </p>
         */
        Thread.sleep(5000);
    }

}

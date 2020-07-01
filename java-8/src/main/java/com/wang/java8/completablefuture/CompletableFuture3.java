package com.wang.java8.completablefuture;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description: Completable 流水线，任务处理完成后对数据继续进行处理
 * @date: 2020/7/1 22:08
 * @author: weiman cui
 */
public class CompletableFuture3 {

    public static void main(String[] args) {
        /*ExecutorService executor = Executors.newFixedThreadPool(2);
        // 调用get方法后，再将结果 扩大10倍
        CompletableFuture.supplyAsync(CompletableFuture1::get, executor)
                .thenApply(CompletableFuture3::multiply)
                .whenComplete((v, t) -> Optional.ofNullable(v).ifPresent(System.out::println));
        executor.shutdown();*/

        demo();
        demo2();
    }

    private static void demo() {
        List<Integer> productionIds = Arrays.asList(1, 2, 3, 4, 5);
        /**
         * 根据 产品ID，模拟获取 价格
         */
        Stream<CompletableFuture<Double>> completableFutureStream = productionIds.stream().map(i ->
                CompletableFuture.supplyAsync(() -> queryProduction(i)));
        /**
         * 对商品的价格 进行加价
         */
        Stream<CompletableFuture<Double>> multiplyFuture = completableFutureStream.map(future ->
                future.thenApply(CompletableFuture3::multiply));
        // 直接异步执行结束，结果还没计算出来
        // List<CompletableFuture<Double>> collect = multiplyFuture.collect(Collectors.toList());

        // join()等待，全部执行完毕后，再将Stream转化成List集合
        List<Double> collect = multiplyFuture.map(CompletableFuture::join).collect(Collectors.toList());
        System.out.println(collect);
    }

    private static void demo2() {
        List<Double> collect = Arrays.asList(1, 2, 3, 4, 5).stream()
                .map(i -> CompletableFuture.supplyAsync(() -> queryProduction(i)))
                .map(future -> future.thenApply(CompletableFuture3::multiply))
                .map(CompletableFuture::join).collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * 模拟 根据商品ID，获取商品的价格
     *
     * @param i 商品ID
     * @return
     */
    private static double queryProduction(int i) {
        return CompletableFuture1.get();
    }


    /**
     * 10倍运算
     *
     * @param value
     * @return
     */
    private static double multiply(double value) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return value * 10;
    }


}

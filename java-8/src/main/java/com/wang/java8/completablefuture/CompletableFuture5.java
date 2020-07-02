package com.wang.java8.completablefuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description: CompletableFuture 常用API 2
 * @author: weiman cui
 * @date: 2020/7/2 9:03
 */
public class CompletableFuture5 {

    public static void main(String[] args) throws InterruptedException {
        // runAfterBoth();
        // applyToEither();
        // acceptEither();
        anyOfAndAllOf();
    }

    private static void anyOfAndAllOf() throws InterruptedException {
        List<CompletableFuture<Double>> collect = Arrays.asList(1, 2, 3, 4).stream()
                .map(i -> CompletableFuture.supplyAsync(CompletableFuture1::get))
                .collect(Collectors.toList());
        // anyOf 只要一个执行结束，就执行下一步
        CompletableFuture.anyOf(collect.toArray(new CompletableFuture[collect.size()]))
                .thenRun(() -> System.out.println("anyOf done"));

        //allOf 都执行结束，才执行下一步
        CompletableFuture.allOf(collect.toArray(new CompletableFuture[collect.size()]))
                .thenRun(() -> System.out.println("allOf done"));
        Thread.currentThread().join();
    }


    /**
     * acceptEither 方法，哪一个CompletableFuture 线程先执行结束，就使用它的返回结果
     *
     * @throws InterruptedException
     */
    private static void acceptEither() throws InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            // System.out.println("I am future 1 ");
            Double a = CompletableFuture1.get();
            System.out.println("Future 1 = " + a);
            return a;
        }).acceptEither(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                // System.out.println("I am future 2 ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Double b = CompletableFuture1.get();
            System.out.println("Future 2 = " + b);
            return b;
        }), System.out::println);

        Thread.currentThread().join();
    }


    /**
     * applyToEither 前一个CompletableFuture线程 or 后面的CompletableFuture
     * 任何一个执行结束了，就执行 applyToEither方法中的Runnable线程。
     *
     * @throws InterruptedException
     */
    private static void applyToEither() throws InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            Double a = CompletableFuture1.get();
            System.out.println("Future 1 = " + a);
            return a;
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Double b = CompletableFuture1.get();
            System.out.println("Future 2 = " + b);
            return b;
        }), v -> v * 10)
                .thenAccept(System.out::println);

        Thread.currentThread().join();
    }

    /**
     * 当 前一个CompletableFuture线程执行结束，后面的也执行结束了，
     * 最后才执行 runAfterBoth中的 Runnable方法（runAfterBoth中的第二个参数是一个线程）
     *
     * @throws InterruptedException
     */
    private static void runAfterBoth() throws InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running.");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }).runAfterBoth(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " is running.");
            return 2;
        }), () -> System.out.println("done"));

        ExecutorService executor = Executors.newFixedThreadPool(2, (r) -> {
            Thread t = new Thread(r);
            t.setDaemon(false);
            return t;
        });
        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("返回 cui");
            return "cui";
        }, executor).runAfterBoth(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("返回 sun");
            return "sun";
        }, executor), () -> System.out.println("run"));

    }


}

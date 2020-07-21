package com.wang.java8.completablefuture;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @description: CompletableFuture的异步调用
 * @author: wei·man cui
 * @date: 2020/7/1 17:21
 */
public class CompletableFuture2 {

    public static void main(String[] args) throws InterruptedException {
        // demo1();
        // demo2();
        // demo3();
        demo4();
    }

    /**
     * <p>
     * 开启的线程，被作为main线程的守护线程，当主线程执行结束，那么守护线程也会接触，因此会没有结果
     * 主线程 Main 调用 Thread.join()方法，等待主线程执行完毕后，才继续向下执行
     * 但是主线程一直没有执行结束(main函数等待自己结束后才会结束等待，因此main函数永远不会结束)
     * ，因此主线程 join()方法一下的程序不会被执行
     * </p>
     *
     * @throws InterruptedException
     */
    private static void demo1() throws InterruptedException {
        CompletableFuture.supplyAsync(CompletableFuture1::get).whenComplete((v, t) -> {
            Optional.ofNullable(v).ifPresent(System.out::println);
            Optional.ofNullable(t).ifPresent(x -> x.printStackTrace());
        });
        System.out.println(" I am not block…… " + Thread.currentThread().getName());

        Thread.currentThread().join(2000);
        System.out.println("I am over  " + Thread.currentThread().getName());
    }

    /**
     * 不适用join方法，使用 子线程结束 标识符，使主线程可以结束
     *
     * @throws InterruptedException
     */
    private static void demo2() throws InterruptedException {
        AtomicBoolean flag = new AtomicBoolean(false);
        CompletableFuture.supplyAsync(CompletableFuture1::get).whenComplete((v, t) -> {
            Optional.ofNullable(v).ifPresent(System.out::println);
            Optional.ofNullable(t).ifPresent(x -> x.printStackTrace());
            flag.set(true);
        });
        System.out.println(" I am not block…… " + Thread.currentThread().getName());

        // 睡眠等待
        while (!flag.get()) {
            Thread.sleep(1);
        }
        System.out.println("I am over  " + Thread.currentThread().getName());
    }


    /**
     * Executors 中的线程不是守护线程，因此执行时 不会退出
     * 可以设置成 守护线程，或者使用 executor.shutdown();
     * 但是守护线程 会随着是主线程的结束而死亡，可能还没来得及启动线程任务就退出了
     */
    private static void demo3() {
        ExecutorService executor = Executors.newFixedThreadPool(2, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        executor.execute(() -> System.out.println("test"));
        // executor.shutdown();
    }

    /**
     * 使用 非守护线程任务 执行
     */
    private static void demo4() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletableFuture.supplyAsync(CompletableFuture1::get, executor).whenComplete((v, t) -> {
            Optional.ofNullable(v).ifPresent(System.out::println);
            Optional.ofNullable(t).ifPresent(x -> x.printStackTrace());
        });
        System.out.println("未阻塞");
    }
}

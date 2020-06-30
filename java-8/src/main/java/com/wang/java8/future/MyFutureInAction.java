package com.wang.java8.future;


import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @description: 手写 Future ，了解Future设计模式的原理
 * @date: 2020/6/30 22:56
 * @author: weiman cui
 */
public class MyFutureInAction {
    public static void main(String[] args) throws InterruptedException {
        /*MyFuture<String> future = invoke(() -> {
            try {
                Thread.sleep(5000);
                return "I am finished";
            } catch (InterruptedException e) {
                return "error";
            }
        });
        // 多线程，会直接打印如下方法
        System.out.println(future.get());
        while (!future.isDone()) {
            Thread.sleep(10);
        }
        // 直到 future线程执行完毕，才获取值
        System.out.println(future.get());*/
        String value = block(() -> {
            try {
                Thread.sleep(5000);
                return "I am finished";
            } catch (InterruptedException e) {
                return "error";
            }
        });
        System.out.println(value);
    }

    private static <T> T block(MyCallable<T> callable) {
        return callable.action();
    }


    /**
     * 自定义多线程，操作自定义的Future和Callable接口
     *
     * @param callable
     * @param <T>
     * @return
     */
    private static <T> MyFuture<T> invoke(MyCallable<T> callable) {
        AtomicReference<T> result = new AtomicReference<>();
        AtomicBoolean finished = new AtomicBoolean(false);

        Thread t = new Thread(() -> {
            result.set(callable.action());
            finished.set(true);
        });
        t.start();

        MyFuture<T> future = new MyFuture<T>() {
            @Override
            public T get() {
                return result.get();
            }

            @Override
            public boolean isDone() {
                return finished.get();
            }
        };
        return future;
    }

    /**
     * 自定义Future接口
     *
     * @param <T>
     */
    private interface MyFuture<T> {
        T get();

        boolean isDone();
    }

    /**
     * 自定义Callable接口
     *
     * @param <T>
     */
    private interface MyCallable<T> {
        T action();
    }

}

package com.wang.java8.future;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @description: 异步 基于事件回调的 Future程序
 * @author: weiman cui
 * @date: 2020/7/1 12:37
 */
public class FutureInAction {

    public static void main(String[] args) {
        MyFuture<String> future = invoke(() -> {
            try {
                Thread.sleep(5000L);
                return "I am Finished.";
            } catch (InterruptedException e) {
                return "I am Error.";
            }
        });
        // 线程执行完毕后，执行如下回调函数，线程回调函数 会 进行通知
        future.setCompletable(new Completable<String>() {
            @Override
            public void complete(String s) {
                System.out.println(s);
            }

            @Override
            public void exception(Throwable cause) {
                System.out.println("error");
            }
        });
        System.out.println("=====   =======");
        System.out.println(future.get());
    }


    private static <T> MyFuture<T> invoke(MyCallable<T> callable) {
        AtomicReference<T> result = new AtomicReference<>();
        AtomicBoolean finished = new AtomicBoolean(false);
        // 调用MyFuture接口
        MyFuture<T> future = new MyFuture<T>() {
            private Completable<T> completable;

            @Override
            public T get() {
                return result.get();
            }

            @Override
            public boolean isDone() {
                return finished.get();
            }

            @Override
            public void setCompletable(Completable<T> completable) {
                this.completable = completable;
            }

            @Override
            public Completable<T> getCompletable() {
                return completable;
            }
        };

        Thread t = new Thread(() -> {
            try {
                T value = callable.action();
                result.set(value);
                finished.set(true);
                // 回调函数不为空，则 进行通知
                if (future.getCompletable() != null) {
                    future.getCompletable().complete(value);
                }
            } catch (Throwable cause) {
                if (future.getCompletable() != null) {
                    future.getCompletable().exception(cause);
                }
            }
        });
        t.start();
        return future;
    }

    /**
     * 自定义 带 回调函数的 Future接口
     *
     * @param <T>
     */
    private interface MyFuture<T> {
        // 获取 结果
        T get();

        // 是否执行完毕
        boolean isDone();

        // 回调函数
        void setCompletable(Completable<T> completable);

        Completable<T> getCompletable();
    }

    /**
     * 自定义Callable接口
     *
     * @param <T>
     */
    private interface MyCallable<T> {
        T action();
    }

    /**
     * 自定义 回调函数 接口
     *
     * @param <T>
     */
    private interface Completable<T> {
        void complete(T t);

        void exception(Throwable cause);
    }


}

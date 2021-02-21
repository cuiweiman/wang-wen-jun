package com.wang.netty.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/2/21 18:06
 */
public class ExecutorUtils {

    public static ThreadPoolExecutor getExecutor() {
        return new ThreadPoolExecutor(
                16, 16, 5, SECONDS,
                new LinkedBlockingQueue<>(16),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

}

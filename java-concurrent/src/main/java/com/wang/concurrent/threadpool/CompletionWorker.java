package com.wang.concurrent.threadpool;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * CompletionService 任务类
 */
public class CompletionWorker implements Callable<Integer> {

    private String name;

    public CompletionWorker(String name) {
        this.name = name;
    }

    @Override
    public Integer call() throws Exception {
        int sleepTime = new Random().nextInt(1000);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sleepTime;
    }
}

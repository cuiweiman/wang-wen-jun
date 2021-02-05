package com.wang.concurrent.classutils;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

/**
 * 循环栅栏
 * 线程执行到一个 逻辑点 后，被阻塞，等待其他线程都执行到这个点，然后全部线程继续执行。
 */
public class CyclicBarrierDemo {

    // parties：工作线程的个数
    // private static CyclicBarrier barrier = new CyclicBarrier(5);

    // parties：工作线程的个数；  barrierAction：所以线程到达屏障后，开始执行的另一个线程
    private static CyclicBarrier barrier = new CyclicBarrier(5, new CollectThread());

    private static ConcurrentHashMap<String, Long> resultMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        for (int i = 0; i <= 4; i++) {
            Thread thread = new Thread(new SubThread());
            thread.start();
        }
    }

    /**
     * 负责 屏障 开放后 的工作
     */
    private static class CollectThread implements Runnable {
        @Override
        public void run() {
            StringBuilder result = new StringBuilder();
            for (Map.Entry<String, Long> work : resultMap.entrySet()) {
                result.append("{" + work.getValue() + "}");
            }
            System.out.println(" the result = " + result);
            System.out.println(" do other business…… ");
        }
    }

    /**
     * 工作线程
     */
    private static class SubThread implements Runnable {
        @Override
        public void run() {
            long id = Thread.currentThread().getId();
            resultMap.put(Thread.currentThread().getId() + "", id);
            Random r = new Random();
            try {
                if (r.nextBoolean()) {
                    Thread.sleep(1000 + id);
                    System.out.println("SubThread_" + id + " do something");
                }
                System.out.println(id + " is await ");
                // 等待其他线程 到达本状态，然后才能继续执行
                barrier.await();
                Thread.sleep(1000 + id);
                System.out.println("SubThread_" + id + "…… do its business");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

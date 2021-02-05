package com.wang.concurrent.practice;

import com.wang.concurrent.blockingqueue.ItemVO;
import com.wang.concurrent.blockingqueue.Order;

import java.util.concurrent.DelayQueue;

/**
 * 任务完成后，在一定时间内供查询，之后Wi释放资源以节约内存，需要定期处理过期的任务
 */
public class CheckJobProcessor {
    private static DelayQueue<ItemVO<String>> queue = new DelayQueue<>();

    // 单例模式
    private CheckJobProcessor() {
    }

    private static class ProcessorHolder {
        public static CheckJobProcessor processor = new CheckJobProcessor();
    }

    public static CheckJobProcessor getInstance() {
        return ProcessorHolder.processor;
    }

    private static class FetchJob implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    // 拿到已经过期的任务
                    ItemVO<String> item = queue.take();
                    String jobName = item.getData();
                    PendingJobPool.getMap().remove(jobName);
                    System.out.println(jobName + " is out of date,remove from map!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 任务完成后，放入队列，经过expireTime时间后，从整个框架中移除
     *
     * @param jobName
     * @param expireTime
     */
    public void putJob(String jobName, long expireTime) {
        ItemVO<String> itemVO = new ItemVO<>(expireTime, jobName);
        queue.offer(itemVO);
        System.out.println("Job[" + jobName + "]已经放入了过期检查缓存，过期时长：" + expireTime);
    }

    static {
        Thread thread = new Thread(new FetchJob());
        thread.setDaemon(true);
        thread.start();
        System.out.println("开启任务过期检查 守护线程 成功……");
    }
}

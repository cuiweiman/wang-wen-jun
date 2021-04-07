package com.wang.boot.zookeeper.zkchapter3;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 测试
 * @author: wei·man cui
 * @date: 2021/4/7 10:07
 */
public class DistributedQueueTest {
    private static final String CONNECT_STRING = "192.168.96.128";

    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    @Test
    public void testDistributedQueue() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 5);
        // CuratorFramework client = CuratorFrameworkFactory.newClient(CONNECT_STRING, retryPolicy);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .retryPolicy(retryPolicy)
                .build();
        client.start();

        DistributedQueue<SendObject> queue = new DistributedQueue<>(client, "/Queue");

        SendObject sendObject = new SendObject(String.valueOf(100), "content" + 100);
        queue.offer(sendObject);
        System.out.println("发送一条消息成功：" + sendObject);

        SendObject poll = queue.poll();
        System.out.println("消费一条消息成功：" + poll);

       /* new Thread(new ConsumerThread(queue)).start();
        new Thread(new ProducerThread(queue)).start();
        Thread.currentThread().join();*/
    }
}

class ConsumerThread implements Runnable {
    private DistributedQueue<SendObject> queue;

    public ConsumerThread(DistributedQueue<SendObject> queue) {
        this.queue = queue;
    }

    private static final Integer LOOP = 100;

    @Override
    public void run() {
        for (int i = 0; i < LOOP; i++) {
            try {
                SendObject sendObject = queue.poll();
                System.out.println("消费一条消息成功：" + sendObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class ProducerThread implements Runnable {

    private DistributedQueue<SendObject> queue;

    public ProducerThread(DistributedQueue<SendObject> queue) {
        this.queue = queue;
    }

    private static final Integer LOOP = 100;

    @Override
    public void run() {
        for (int i = 0; i < LOOP; i++) {
            try {
                SendObject sendObject = new SendObject(String.valueOf(i), "content" + i);
                queue.offer(sendObject);
                System.out.println("发送一条消息成功：" + sendObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

class SendObject implements Serializable {

    private static final long serialVersionUID = 1L;

    public SendObject(String id, String content) {
        this.id = id;
        this.content = content;
    }

    private String id;

    private String content;

    @Override
    public String toString() {
        return "SendObject [id=" + id + ", content=" + content + "]";
    }

}

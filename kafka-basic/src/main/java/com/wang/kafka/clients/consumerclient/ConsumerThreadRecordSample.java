package com.wang.kafka.clients.consumerclient;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: 多线程 监听 消息 并 分发处理器处理消息
 * @author: cuiweiman
 * @date: 2021/11/11 10:06
 * @see ExecutorService#shutdown() 平滑关闭线程池。线程池的状态则立刻变成SHUTDOWN状态。线程池停止接收新任何任务，
 * 否则将会抛出 RejectedExecutionException 异常。并等待已经提交（正在执行和已提交未执行）的任务执行完成，关闭线程池。
 * @see ExecutorService#shutdownNow() 线程池的状态立刻变成STOP状态，并调用 Thread.interrupt() 方法试图停止所有正在执行的线程，
 * 不再处理还在池队列中等待的任务，并返回未执行的任务。但并不代表线程池就一定立即就能退出，它可能必须要等待所有正在执行的任务都执行
 * 完成了才能退出(原因是因为 interrupt() 方法的局限性)。
 * @see Thread#interrupt() 中断线程。如果线程中没有sleep 、wait、Condition、定时锁等应用, 是无法中断当前的线程的。即当有线程处于
 * Running 执行状态时，那么无法被中断，需要等 Running 状态的线程执行结束或异常退出。
 * @see ExecutorService#awaitTermination(long, TimeUnit) 等待线程池线程执行结束。若时间内结束，则返回true；超时则返回false。
 */
public class ConsumerThreadRecordSample {

    private static final String TOPIC_NAME = "admin_topic";

    public static void main(String[] args) throws InterruptedException {
        String broker = "localhost:9092";
        String groupId = "test";
        ConsumerMessage consumerMessage = new ConsumerMessage(broker, groupId, TOPIC_NAME);
        int workThreadNum = 5;

        consumerMessage.pollMessage(workThreadNum);

        // 根本 执行不到
        TimeUnit.SECONDS.sleep(10);
        consumerMessage.shutdown();
    }


    /**
     * Kafka 消费者线程，拉取消息后，分发给 消息处理线程进行消息处理
     */
    static class ConsumerMessage {

        private KafkaConsumer<String, String> consumer;
        private ExecutorService executorService;

        public ConsumerMessage(String brokerList, String groupId, String topic) {
            Properties properties = new Properties();
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
            properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
            properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
            properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
            properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Collections.singletonList(topic));
        }

        public void pollMessage(int workThread) throws InterruptedException {
            System.out.println("【当前线程名称：】" + Thread.currentThread().getName());
            executorService = new ThreadPoolExecutor(workThread, workThread, 0L,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
            while (true) {
                TimeUnit.MILLISECONDS.sleep(100);
                final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    executorService.submit(new ConsumerRecordWorker(record));
                }
            }
        }

        /**
         * 关闭 消费者线程 和 消费者客户端
         */
        public void shutdown() {
            System.out.println("begin to shutdown the KafkaConsumer ExecutorService");
            if (Objects.nonNull(consumer)) {
                consumer.close();
            }
            if (Objects.nonNull(executorService) && !executorService.isShutdown()) {
                executorService.shutdown();
            }
            try {
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.out.println("Thread shutdown timeout, ignore this case.");
                }
            } catch (InterruptedException e) {
                System.out.println("Other thread interrupted this shutdown, ignore this case.");
                Thread.currentThread().interrupt();
            }
        }
    }
}


class ConsumerRecordWorker implements Runnable {
    private ConsumerRecord record;

    public ConsumerRecordWorker(ConsumerRecord record) {
        this.record = record;
    }

    @Override
    public void run() {
        // 处理 消息记录
        System.out.println("Thread - " + Thread.currentThread().getName());
        System.err.printf("【数据库记录数据】partition = %d , offset = %d, key = %s, value = %s%n",
                record.partition(), record.offset(), record.key(), record.value());
    }
}



package com.wang.kafka.clients.consumerclient;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @description: 多线程 消费者
 * @author: cuiweiman
 * @date: 2021/11/10 16:11
 */

public class ConsumerThreadSample {

    public static void main(String[] args) throws InterruptedException {
        KafkaConsumerRunnable runner = new KafkaConsumerRunnable();
        Thread thread = new Thread(runner);
        thread.start();
        Thread.sleep(15000);
        runner.shutdown();
    }
}

@Slf4j
class KafkaConsumerRunnable implements Runnable {
    public static final String TOPIC_NAME = "admin_topic";
    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    private final KafkaConsumer<String, String> consumer;

    public KafkaConsumerRunnable() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(properties);

        TopicPartition p0 = new TopicPartition(TOPIC_NAME, 0);
        TopicPartition p1 = new TopicPartition(TOPIC_NAME, 1);
        TopicPartition p2 = new TopicPartition(TOPIC_NAME, 2);
        consumer.assign(Lists.newArrayList(p0));
    }

    @Override
    public void run() {
        try {
            // 没关闭，则消费消息
            while (!isClosed.get()) {
                final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (TopicPartition partition : records.partitions()) {
                    final List<ConsumerRecord<String, String>> recordList = records.records(partition);
                    for (ConsumerRecord<String, String> record : recordList) {
                        log.info("记录数据到数据库：partition = {}, offset = {},  key = {}, value = {}",
                                record.partition(), record.offset(), record.key(), record.value());
                    }
                    final long offset = recordList.get(recordList.size() - 1).offset();
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(offset + 1)));
                    System.out.println("------------------ partition = " + partition + " END ------------------");
                }
            }
        } catch (WakeupException e) {
            if (!isClosed.get()) {
                throw e;
            }
        } finally {
            consumer.close();
        }
    }

    public void shutdown() {
        isClosed.set(true);
        // 唤醒消费者。此方法是线程安全的，适用于中止长轮询。
        consumer.wakeup();
    }
}
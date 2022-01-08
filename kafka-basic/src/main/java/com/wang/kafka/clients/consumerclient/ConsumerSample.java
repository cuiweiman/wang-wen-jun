package com.wang.kafka.clients.consumerclient;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @description: consumer sample
 * @author: cuiweiman
 * @date: 2021/11/9 16:04
 * @see ConsumerSample#helloWorld() 消费者客户端 参数介绍
 * @see ConsumerSample#commitOffset() 手动提交 消息的偏移量 offset
 * @see ConsumerSample#commitOffsetWithPartition() 手动提交 消息的偏移量 offset
 * @see ConsumerSample#commitOffsetWithPartitionAdvanced() 手动订阅指定的 一个或多个 分区，并手动提交当前消费的偏移量
 * @see ConsumerSample#commitOffsetWithPartitionAdvancedAndSetOffset() 设置起始偏移量，手动订阅指定的一个或多个分区，手动提交当前消费的偏移量
 * @see ConsumerSample#kafkaConsumerLimit() 消费者客户端 限流
 * @see ConsumerThreadSample 多线程消费
 * @see ConsumerThreadRecordSample 多线程 监听 消息 并 分发处理器处理消息
 */
@Slf4j
public class ConsumerSample {

    private static final String TOPIC_NAME = "admin_topic";

    public static void main(String[] args) {
        // 消费者参数介绍
        // helloWorld();

        //  消息偏移量 的 手动提交
        // commitOffset();

        // 消息偏移量 的 手动提交 并
        // commitOffsetWithPartition();

        // 手动控制一到多个 Partition 分区
        // commitOffsetWithPartitionAdvanced();

        // 指定起始偏移量，手动控制一到多个 Partition 分区
        // commitOffsetWithPartitionAdvancedAndSetOffset();

        // 消费者客户端 限流
        kafkaConsumerLimit();
    }


    /**
     * 使用方式不推荐：自动提交
     */
    public static void helloWorld() {
        Properties properties = new Properties();
        // 设置 broker 服务器
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // 设置 消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        // 自动提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        // 自动提交的间隔
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        // key-value的编码
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Lists.newArrayList(TOPIC_NAME));

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            // 每 1000ms->1s 批量拉取一次数据
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("partition = %d, offset = %d,  key = %s, value = %s %n",
                        record.partition(), record.offset(), record.key(), record.value());
            }
        }
    }


    /**
     * 手动提交：
     * 自动提交时，会自动提交 offset 偏移量，提交过的消息将无法再次被消费。若数据处理耗时或处理失败，则会导致数据丢失。
     * 因此要手动提交。
     */
    public static void commitOffset() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Lists.newArrayList(TOPIC_NAME));

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                log.info("记录数据到数据库：partition = {}, offset = {},  key = {}, value = {}",
                        record.partition(), record.offset(), record.key(), record.value());
                // 如果失败了，则回滚数据，不向 kafka 提交 offset
            }
            // 提交成功了，则手动提交 offset
            consumer.commitAsync();
        }
    }


    /**
     * 手动提交，并分区消费(消费指定Partition)
     */
    public static void commitOffsetWithPartition() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Lists.newArrayList(TOPIC_NAME));

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (TopicPartition partition : records.partitions()) {
                final List<ConsumerRecord<String, String>> recordList = records.records(partition);
                for (ConsumerRecord<String, String> record : recordList) {
                    log.info("记录数据到数据库：partition = {}, offset = {},  key = {}, value = {}",
                            record.partition(), record.offset(), record.key(), record.value());
                    // 如果失败了，则回滚数据，不向 kafka 提交 offset
                }
                // 获取当前 消费过的 消息的 偏移量
                long lastOffset = recordList.get(recordList.size() - 1).offset();
                Map<TopicPartition, OffsetAndMetadata> offset = Maps.newHashMap();
                // 设置 下一个 消息的 偏移量
                offset.put(partition, new OffsetAndMetadata(lastOffset + 1));
                // 提交成功了，则手动提交 当前 partition 内的 offset。
                consumer.commitSync(offset);
                System.out.println("------------------ partition = " + partition + " END ------------------");
            }
        }
    }


    /**
     * 手动订阅指定的 一个或多个 分区，并手动提交
     */
    public static void commitOffsetWithPartitionAdvanced() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        // KafkaConsumer 直接订阅 topic 的指定 分区
        TopicPartition partitionA = new TopicPartition(TOPIC_NAME, 0);
        TopicPartition partitionB = new TopicPartition(TOPIC_NAME, 1);
        consumer.assign(Lists.newArrayList(partitionA));

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                log.info("记录数据到数据库：partition = {}, offset = {},  key = {}, value = {}",
                        record.partition(), record.offset(), record.key(), record.value());
                // 如果失败了，则回滚数据，不向 kafka 提交 offset
            }
            // 获取当前 消费过的 消息的 偏移量
            long lastOffset = records.count();
            Map<TopicPartition, OffsetAndMetadata> offset = Maps.newHashMap();
            // 设置 下一个 消息的 偏移量
            offset.put(partitionA, new OffsetAndMetadata(lastOffset + 1));
            // 提交成功了，则手动提交 当前 partition 内的 offset。
            consumer.commitSync(offset);
            System.out.println("------------------ partition = " + partitionA + " END ------------------");
        }
    }


    public static void commitOffsetWithPartitionAdvancedAndSetOffset() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        // KafkaConsumer 直接订阅 topic 的指定 分区
        TopicPartition partitionA = new TopicPartition(TOPIC_NAME, 0);
        TopicPartition partitionB = new TopicPartition(TOPIC_NAME, 1);
        consumer.assign(Lists.newArrayList(partitionA));

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            // 手动指定 起始 的 偏移量 offset
            /*
             * 场景一：第一次从 0 offset 消费，若第一次消费了 100 条，那么 redis 记录 offset 为 100；
             * 再次消费前，从 redis 中拿到偏移量，从该位置开始继续消费。
             * 场景二：消费失败时，记录 offset 位置，从新消费。
             */
            consumer.seek(partitionA, 295);
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                log.info("记录数据到数据库：partition = {}, offset = {},  key = {}, value = {}",
                        record.partition(), record.offset(), record.key(), record.value());
                // 如果失败了，则回滚数据，不向 kafka 提交 offset
            }
            // 获取当前 消费过的 消息的 偏移量
            long lastOffset = records.count();
            Map<TopicPartition, OffsetAndMetadata> offset = Maps.newHashMap();
            // 设置 下一个 消息的 偏移量
            offset.put(partitionA, new OffsetAndMetadata(lastOffset + 1));
            // 提交成功了，则手动提交 当前 partition 内的 offset。
            consumer.commitSync(offset);
            System.out.println("------------------ partition = " + partitionA + " END ------------------");
        }
    }


    /**
     * Consumer 限流
     */
    public static void kafkaConsumerLimit() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        // KafkaConsumer 直接订阅 topic 的指定 分区
        TopicPartition partitionA = new TopicPartition(TOPIC_NAME, 0);
        TopicPartition partitionB = new TopicPartition(TOPIC_NAME, 1);
        consumer.assign(Lists.newArrayList(partitionA));

        // 阈值
        long totalNum = 50;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            // 手动指定 起始 的 偏移量 offset
            consumer.seek(partitionA, 295);
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            long num = 0;
            for (ConsumerRecord<String, String> record : records) {
                log.info("记录数据到数据库：partition = {}, offset = {},  key = {}, value = {}",
                        record.partition(), record.offset(), record.key(), record.value());
                num++;
                // 指定分区 的 消费者 消费达到一定数量时，暂停从该分区的消息消费
                if (record.partition() == 0) {
                    if (num >= totalNum) {
                        // 暂停 从  partitionA 分区消费消息
                        consumer.pause(Collections.singletonList(partitionA));
                    }
                }
                if (record.partition() == 1) {
                    if (num == 50) {
                        // 继续 从  partitionA 分区消费消息
                        consumer.resume(Collections.singletonList(partitionA));
                    }
                }

                // 如果失败了，则回滚数据，不向 kafka 提交 offset
            }
            // 获取当前 消费过的 消息的 偏移量
            long lastOffset = records.count();
            Map<TopicPartition, OffsetAndMetadata> offset = Maps.newHashMap();
            // 设置 下一个 消息的 偏移量
            offset.put(partitionA, new OffsetAndMetadata(lastOffset + 1));
            // 提交成功了，则手动提交 当前 partition 内的 offset。
            consumer.commitSync(offset);
            System.out.println("------------------ partition = " + partitionA + " END ------------------");
        }
    }

}

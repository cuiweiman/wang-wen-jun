package com.wang.kafka.clients.consumerclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.assertj.core.util.Lists;

import java.time.Duration;
import java.util.*;

/**
 * Kafka 0.9 版本之前，offset 存储在 zookeeper，0.9 版本及之后，默认将 offset 存储在 Kafka 的一个内置的 topic 中。除此之外，
 * Kafka 还可以选择自定义存储 offset。offset 的维护是相当繁琐的，因为需要考虑到消费者的 Rebalace。
 * 当有新的消费者加入消费者组、已有的消费者推出消费者组或者所订阅的主题的分区发生变化，就会触发到分区的重新分配，重新分配的过程叫做 Rebalance。
 * 消费者发生 Rebalance 之后，每个消费者消费的分区就会发生变化。因此消费者要首先获取到自己被重新分配到的分区，并且定位到每个分区最近提交的 offset 位置继续消费。
 * 要实现自定义存储 offset，需要借助 ConsumerRebalanceListener，以下为示例代码，其中提交和获取 offset 的方法，需要根据所选的 offset 存储系统自行实现。
 * <p>
 * https://blog.csdn.net/xdkb159/article/details/110916672
 *
 * @description: 自定义存储 offset 偏移量
 * @author: cuiweiman
 * @date: 2022/1/25 10:04
 * @see #commitSaveOffset() 自定义 ConsumerRebalanceListener 记录 消费的 offset 信息
 */
@Slf4j
public class ConsumerSampleAdvance {

    private static final String TOPIC_NAME = "admin_topic";

    public static void main(String[] args) {
        // 异步提交，回调异常
        // commitAsync();

        // 自定义存储 Offset 偏移量
        commitSaveOffset();
    }

    /**
     * 异步提交，异常回调
     */
    public static void commitAsync() {
        KafkaConsumer<String, String> consumer = createConsumer();
        consumer.subscribe(Lists.newArrayList(TOPIC_NAME));

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            ConsumerRecords<String, String> poll = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : poll) {
                log.info("记录数据到数据库：partition = {}, offset = {},  key = {}, value = {}",
                        record.partition(), record.offset(), record.key(), record.value());
            }
            // 提交成功了，则手动提交 offset
            consumer.commitAsync((offsets, exception) -> {
                if (Objects.nonNull(exception)) {
                    log.error("commit failed for {} ", offsets);
                }
            });
        }
    }

    /**
     * 自定义存储 Offset 偏移量
     */

    public static final Map<TopicPartition, Long> CONCURRENT_OFFSET = new HashMap<>();

    public static void commitSaveOffset() {
        KafkaConsumer<String, String> consumer = createConsumer();
        consumer.subscribe(Lists.newArrayList(TOPIC_NAME), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                // re-balance 之前调用，异步提交所有的 offsets
                for (TopicPartition partition : partitions) {
                    CONCURRENT_OFFSET.put(partition, consumer.position(partition));
                }
                // commitOffset(CONCURRENT_OFFSET);
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                // re-balance 之后调用
                CONCURRENT_OFFSET.clear();
                for (TopicPartition partition : partitions) {
                    // 定位到 最近提交的 offset，继续消费
                    consumer.seek(partition, getOffset(partition));
                }
            }
        });

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            ConsumerRecords<String, String> poll = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : poll) {
                log.info("记录数据到数据库：partition = {}, offset = {},  key = {}, value = {}",
                        record.partition(), record.offset(), record.key(), record.value());
                // 存储的 offset 要加1，避免重复消费同一个offset的消息
                CONCURRENT_OFFSET.put(new TopicPartition(record.topic(), record.partition()), record.offset() + 1);
            }
            // 偏移量信息 存储在 concurrentOffset
        }
    }

    /**
     * 异步提交所有的 offsets，并存储在本地
     *
     * @param concurrentOffset 分区-offset 关系
     */
    public static void commitOffset(Map<TopicPartition, Long> concurrentOffset) {
        // 已经存入了 concurrentOffset，故此处不需要做其它处理
        // TODO partition 对应的 offset 信息 入库
    }

    /**
     * 获取本地存储的 分区对应的 offset
     *
     * @param partition 分区
     * @return offset
     */
    public static Long getOffset(TopicPartition partition) {
        return Optional.ofNullable(CONCURRENT_OFFSET.get(partition)).orElse(0L);
    }


    private static KafkaConsumer<String, String> createConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Lists.newArrayList(TOPIC_NAME));
        return consumer;
    }
}

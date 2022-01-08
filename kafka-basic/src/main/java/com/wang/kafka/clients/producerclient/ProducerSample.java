package com.wang.kafka.clients.producerclient;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @description: producer client demo
 * @author: cuiweiman
 * @date: 2021/10/27 20:40
 * @see ProducerSample#producerSend() 异步发送演示
 * @see ProducerSample#producerSendBlocked()  异步阻塞发送 / 同步消息发送
 * @see ProducerSample#producerSendAsyncCallback() 异步回调发送
 * @see ProducerSample#producerSendAsyncCallbackWithCustomPartition() 异步回调发送, 自定义 分区 负载均衡
 * @see com.wang.kafka.clients.producerclient.PartitionSample 自定义 Partition 分区 的负载均衡
 */
@Slf4j
public class ProducerSample {

    public static final String TOPIC_NAME = "admin_topic";

    public static void main(String[] args) {

        // KafkaProducer 异步发送演示
        // producerSend();

        // KafkaProducer 异步阻塞发送演示 / 同步消息发送
        // producerSendBlocked();

        // KafkaProducer 异步回调发送
        // producerSendAsyncCallback();

        // KafkaProducer 异步回调发送, 自定义 分区的 负载均衡
        producerSendAsyncCallbackWithCustomPartition();
    }


    /**
     * KafkaProducer 异步发送演示
     */
    public static void producerSend() {
        final Properties properties = new Properties();
        // broker server 的地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // producer 需要在 server 接收到数据后回复确认信号。
        // 0：表示不需要等待server的回复。
        // 1：表示至少要等leader节点将数据写入本地log，不必等待follower写入成功。（若此时leader挂掉消息会丢失）
        // all：leader要等待所有的节点都写入成功才回复确认信号，只要有一个备份存活就不会丢失数据，是最强保证
        properties.put(ProducerConfig.ACKS_CONFIG, "all");

        // 生产者从服务器收到的错误有可能是临时性的错误（比如分区找不到首领）。在这种情况下， retries参数的值决定了生产者可以重发消息的次数，
        // 如果达到这个次数，生产者会放弃重试并返回错误。默认情况下，生产者会在每次重试之间等待100ms ，可以通过 retry.backoff.ms 参数来配置时间间隔。
        properties.put(ProducerConfig.RETRIES_CONFIG, "1");

        // Producer 将批处理消息记录，以减少client与server的请求次数。默认 16384
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        // 批次发送间隔
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");

        // key 和 value 的序列化与反序列化
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // 生产者
        final Producer<String, String> producer = new KafkaProducer<>(properties);
        // 消息对象 ProducerRecord
        for (int i = 0; i < 5; i++) {
            ProducerRecord<String, String> record =
                    new ProducerRecord<>(TOPIC_NAME, "msgKey-" + i, "msgContent" + i);
            producer.send(record);
        }
        producer.close();
    }


    /**
     * KafkaProducer 异步阻塞发送演示 / 同步消息发送
     */
    @SneakyThrows
    public static void producerSendBlocked() {
        final Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, "1");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        final Producer<String, String> producer = new KafkaProducer<>(properties);
        for (int i = 0; i < 10; i++) {
            String key = "msgKey-" + i;
            ProducerRecord<String, String> record =
                    new ProducerRecord<>(TOPIC_NAME, key, "msgContent" + i);
            final Future<RecordMetadata> future = producer.send(record);
            final RecordMetadata metadata = future.get();
            log.info("key={}, partition={}, offset={}", key, metadata.partition(), metadata.offset());
        }
        producer.close();
    }

    /**
     * KafkaProducer 异步回调发送
     */
    @SneakyThrows
    public static void producerSendAsyncCallback() {
        final Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, "1");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        final Producer<String, String> producer = new KafkaProducer<>(properties);
        for (int i = 0; i < 10; i++) {
            String key = "msgKey-" + i;
            ProducerRecord<String, String> record =
                    new ProducerRecord<>(TOPIC_NAME, key, "msgContent" + i);

            producer.send(record, (metadata, e) -> {
                log.info(" partition={}, offset={}", metadata.partition(), metadata.offset());
            });
        }
        producer.close();
    }

    /**
     * KafkaProducer 异步回调发送, 自定义 分区的 负载均衡
     */
    @SneakyThrows
    public static void producerSendAsyncCallbackWithCustomPartition() {
        final Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, "1");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        // 配置自定义分区的负载均衡
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.wang.kafka.clients.producerclient.PartitionSample");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        final Producer<String, String> producer = new KafkaProducer<>(properties);
        for (int i = 0; i < 100; i++) {
            String key = "msgKey-" + i;
            ProducerRecord<String, String> record =
                    new ProducerRecord<>(TOPIC_NAME, key, "msgContent" + i);

            producer.send(record, (metadata, e) -> {
                log.info(" partition={}, offset={}", metadata.partition(), metadata.offset());
            });
        }
        producer.close();
    }

}

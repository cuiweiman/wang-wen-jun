package com.wang.kafka.wechat.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @description: kafka config
 * @author: cuiweiman
 * @date: 2021/11/8 16:55
 */
@Configuration
public class KafkaConfig {

    @Value("${custom.kafka.bootstrap}")
    private String bootstrap;

    @Value("${custom.kafka.acks}")
    private String acks;

    @Value("${custom.kafka.retries}")
    private String retries;

    @Value("${custom.kafka.batchSize}")
    private String batchSize;

    @Value("${custom.kafka.linger}")
    private String linger;

    @Bean
    public Producer<String, Object> kafkaProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        properties.put(ProducerConfig.ACKS_CONFIG, acks);
        properties.put(ProducerConfig.RETRIES_CONFIG, retries);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(properties);
    }

}

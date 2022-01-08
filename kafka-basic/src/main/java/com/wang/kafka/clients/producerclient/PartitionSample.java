package com.wang.kafka.clients.producerclient;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * @description: 自定义 分区选择
 * @author: cuiweiman
 * @date: 2021/11/5 20:01
 */
public class PartitionSample implements Partitioner {
    /**
     * 只有一个 partition，key格式为： key-1,key-2...。
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        String num = key.toString().substring(7);
        final int i = Integer.parseInt(num);
        return i - i;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}

package com.wang.rocketmq.basic.chapter01;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * @description: 消息消费
 * @author: wei·man cui
 * @date: 2021/3/5 14:39
 */
public class SimpleConsumer {

    private static final String NAME_SERVER = "ip:port";

    public static void consumer(String group, String topic, String tags) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);
        consumer.setNamesrvAddr(NAME_SERVER);
        consumer.subscribe(topic, tags);
        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
            String s = new String(msg.get(0).getBody());
            System.out.printf("%s Receive New Messages: %s %s %n", Thread.currentThread().getName(), msg, s);
            // 标记该消息已经被成功消费
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        System.out.println("Consumer Started");
    }

    public static void main(String[] args) throws MQClientException {
        // consumer("SYNC_PRODUCER_TEST", "SYNC_TOPIC", "SYNC_TAGS");
        consumer("ASYNC_PRODUCER_TEST", "ASYNC_TOPIC", "ASYNC_TAGS");
        // consumer("ONE_WAY_PRODUCER_TEST", "ONE_WAY_TOPIC", "ONE_WAY_TAGS");
    }
}

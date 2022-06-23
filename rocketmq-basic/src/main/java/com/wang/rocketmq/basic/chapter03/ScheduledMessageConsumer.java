package com.wang.rocketmq.basic.chapter03;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * @description: 就是普通的消费
 * @date: 2021/4/10 19:23
 * @author: wei·man cui
 */
public class ScheduledMessageConsumer {
    private static final String NAME_SERVER = "ip:port";

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ScheduledProducerGroup");
        consumer.setNamesrvAddr(NAME_SERVER);
        consumer.subscribe("Scheduled-Topic", "Scheduled-Tags");
        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
            String s = new String(msg.get(0).getBody());
            System.out.printf("%s Receive Scheduled Messages: %s %s %n", Thread.currentThread().getName(), msg, s);
            // 标记该消息已经被成功消费
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        System.out.println("Consumer Started");
    }
}

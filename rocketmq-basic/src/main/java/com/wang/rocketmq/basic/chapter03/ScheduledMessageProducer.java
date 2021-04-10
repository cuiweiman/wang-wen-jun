package com.wang.rocketmq.basic.chapter03;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @description: 延迟消息
 * @date: 2021/4/10 19:10
 * @author: wei·man cui
 */
public class ScheduledMessageProducer {

    private static final String NAME_SERVER = "192.168.0.108:9876";

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("ScheduledProducerGroup");
        producer.setNamesrvAddr(NAME_SERVER);
        producer.start();
        int totalMsgSend = 100;
        for (int i = 0; i < totalMsgSend; i++) {
            Message message = new Message("Scheduled-Topic","Scheduled-Tags",
                    ("Scheduled Msg " + i).getBytes());
            // 设置 延时等级为 3，这个消息将在 10s 后发送（现在只支持几个固定的时间）
            //"1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
            message.setDelayTimeLevel(3);
            producer.send(message);
        }
        // 关闭生产者
        producer.shutdown();
    }

}

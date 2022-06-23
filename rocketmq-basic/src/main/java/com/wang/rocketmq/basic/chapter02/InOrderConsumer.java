package com.wang.rocketmq.basic.chapter02;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @description: 分区顺序 消费
 * @author: wei·man cui
 * @date: 2021/3/26 17:10
 */
public class InOrderConsumer {

    private static final String NAME_SERVER = "ip:port";

    public static void main(String[] args) throws Exception {

        final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("Order_Msg_Consumer");
        consumer.setNamesrvAddr(NAME_SERVER);
        /*
        设置 Consumer 第一次启动是从队列头部开始消费，还是队列尾部开始消费。
        如果非第一次启动，那么按照上一次 消费的位置 继续消费。
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("Msg_In_Order_Topic", "TagA||TagB||TagC");
        consumer.registerMessageListener(new MessageListenerOrderly() {
            Random random = new Random();

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                consumeOrderlyContext.setAutoCommit(true);
                for (MessageExt msg : list) {
                    String receive = "consumeThread=".concat(Thread.currentThread().getName())
                            .concat(" queueId=").concat(String.valueOf(msg.getQueueId()))
                            .concat(" content=").concat(new String(msg.getBody()));
                    System.out.println(receive);
                }
                try {
                    // 模拟 业务 处理 逻辑
                    TimeUnit.SECONDS.sleep(random.nextInt(10));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
        System.out.println("Consumer started......");
    }
}

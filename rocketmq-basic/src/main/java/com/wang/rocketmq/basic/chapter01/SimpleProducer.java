package com.wang.rocketmq.basic.chapter01;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 1. 同步消息：可靠性同步的发送方式使用比较广泛，比如：重要的消息通知，短信通知等。
 * 2. 发送异步消息：使用在对响应时间敏感的业务场景，不容忍长时间等地待 Broker 响应。
 * 3. 单向发送消息：不特别关心发送结果的场景。如日志发送。
 *
 * @description: 消息生产者：发送同步消息；发送异步消息；单向发送消息。
 * @author: wei·man cui
 * @date: 2021/3/5 14:38
 */
public class SimpleProducer {

    /**
     * 发送的消息数量
     */
    private static final Integer MSG_COUNT = 100;
    private static final String NAME_SERVER = "139.196.184.230:9876";


    /**
     * 发送同步消息
     *
     * @throws Exception 异常
     */
    public static void syncProducer() throws Exception {
        // 实例化 消息生产者 InOrderProducer，负责生产消息，发送消息
        DefaultMQProducer producer = new DefaultMQProducer("SYNC_PRODUCER_TEST");
        // 设置 NameServer 地址
        producer.setNamesrvAddr(NAME_SERVER);
        // 启动 InOrderProducer 实例
        producer.start();
        for (int i = 0; i < MSG_COUNT; i++) {
            // 设置消息主题，主题是消息订阅的基本单位。设置消息标识，区分同一个主题下不同类型的消息。
            Message message = new Message("SYNC_TOPIC", "SYNC_TAGS",
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult send = producer.send(message);
            System.out.printf("%s%n", send);
        }
        producer.shutdown();
    }

    /**
     * 发送异步消息
     *
     * @throws Exception 异常
     */
    public static void asyncProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("ASYNC_PRODUCER_TEST");
        producer.setNamesrvAddr(NAME_SERVER);
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);

        CountDownLatch2 countDownLatch2 = new CountDownLatch2(MSG_COUNT);

        for (int i = 0; i < MSG_COUNT; i++) {
            final int index = i;
            Message msg = new Message("ASYNC_TOPIC", "ASYNC_TAGS"
                    , "Order_ID_1880", "Hello World".getBytes(StandardCharsets.UTF_8));
            // SendCallback 接收异步返回结果的 回调
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable e) {
                    System.out.printf("%-10d OK %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }
        // 由于是异步发送，因此需要等待 5s 消息发送结束，不然会直接执行下一行代码。
        countDownLatch2.await(5, TimeUnit.SECONDS);
        producer.shutdown();
    }

    /**
     * 单向发送消息
     *
     * @throws Exception 异常
     */
    public static void oneWayProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("ONE_WAY_PRODUCER_TEST");
        producer.setNamesrvAddr(NAME_SERVER);
        producer.start();
        for (int i = 0; i < MSG_COUNT; i++) {
            Message message = new Message("ONE_WAY_TOPIC", "ONE_WAY_TAGS",
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.sendOneway(message);
        }
        producer.shutdown();
    }


    public static void main(String[] args) throws Exception {
        // syncProducer();
        asyncProducer();
        // oneWayProducer();
    }


}

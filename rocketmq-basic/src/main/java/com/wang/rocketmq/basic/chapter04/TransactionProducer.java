package com.wang.rocketmq.basic.chapter04;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: 事务性消息 生产者
 * @date: 2021/4/10 19:49
 * @author: wei·man cui
 */
public class TransactionProducer {
    private static final String NAME_SERVER = "ip:port";

    public static void main(String[] args) throws Exception {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2000), r -> {
            Thread thread = new Thread(r);
            thread.setName("client-transaction-msg-check-thread");
            return thread;
        });

        TransactionListener transactionListener = new TransactionListenerImpl();
        TransactionMQProducer producer = new TransactionMQProducer("Transaction-Producer-Group");
        producer.setExecutorService(executor);
        producer.setTransactionListener(transactionListener);
        producer.setNamesrvAddr(NAME_SERVER);
        producer.start();
        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD"};
        for (int i = 0; i < 10; i++) {
            try {
                Message message = new Message("Transaction-Topic", tags[i % tags.length], "key " + i,
                        ("Hello " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.sendMessageInTransaction(message, null);
                System.out.printf("%s%n", sendResult);
                Thread.sleep(10);
            } catch (MQClientException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(1000);
        }
        producer.shutdown();
    }


}

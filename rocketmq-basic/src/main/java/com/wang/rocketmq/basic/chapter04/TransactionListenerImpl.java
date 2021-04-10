package com.wang.rocketmq.basic.chapter04;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 当发送半消息成功时，我们使用 executeLocalTransaction 方法来执行本地事务。
 * 它返回前一节中提到的三个事务状态之一。
 * checkLocalTransaction 方法用于检查本地事务状态，并回应消息队列的检查请求。
 * 它也是返回前一节中提到的三个事务状态之一。
 *
 * @description: 事务消息的 监听
 * @date: 2021/4/10 19:50
 * @author: wei·man cui
 */
public class TransactionListenerImpl implements TransactionListener {
    private AtomicInteger transactionIndex = new AtomicInteger(0);
    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();


    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        int value = transactionIndex.getAndIncrement();
        int status = value % 3;
        localTrans.put(msg.getTransactionId(), status);
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        Integer status = localTrans.get(msg.getTransactionId());
        if (null != status) {
            switch (status) {
                case 0:
                    return LocalTransactionState.UNKNOW;
                case 1:
                    return LocalTransactionState.COMMIT_MESSAGE;
                case 2:
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                default:
                    break;
            }
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}

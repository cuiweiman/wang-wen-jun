package com.wang.netty.daxin.chapter05rpc.pojo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 同步 返回结果
 * @author: wei·man cui
 * @date: 2021/4/26 12:32
 */
public class SyncResponse {

    private String respId;

    private Object result;

    private AtomicBoolean canGet = new AtomicBoolean(false);

    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public SyncResponse(String respId) {
        this.respId = respId;
    }

    /**
     * 获取执行结果
     * 当不可获取时，阻塞线程
     *
     * @return 结果
     */
    public Object getResult() {
        lock.lock();
        try {
            while (!canGet.get()) {
                // 不能获取，则阻塞当前线程
                condition.await();
            }
            return result;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 设置结果，并将 阻塞的线程环境
     *
     * @param result 结果
     */
    public void setResult(Object result) {
        lock.lock();
        try {
            this.result = result;
            this.canGet.set(true);
            this.condition.signal();
        } finally {
            lock.unlock();
        }
    }

}

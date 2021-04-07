package com.wang.boot.zookeeper.zkchapter4lock;

import java.util.concurrent.TimeUnit;

/**
 * @description: zk实现分布式锁
 * @date: 2021/4/7 22:16
 * @author: wei·man cui
 */
public interface DistributedLockZk {

    /**
     * 尝试获取锁
     *
     * @return 结果
     */
    boolean tryLock();

    /**
     * 加锁
     */
    void lock();

    /**
     * 规定时间内 等待获取锁
     *
     * @param time 时间
     * @param unit 单位
     * @return 结果
     */
    boolean lock(long time, TimeUnit unit);


    /**
     * 释放锁
     */
    void unLock();

}

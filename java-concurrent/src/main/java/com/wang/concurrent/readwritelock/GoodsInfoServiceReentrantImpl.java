package com.wang.concurrent.readwritelock;

import com.wang.concurrent.demo1.SleepTools;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 使用读写锁 ReentrantReadWriteLock 实现
 */
public class GoodsInfoServiceReentrantImpl implements GoodsInfoService {

    private GoodsInfo goodsInfo;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public GoodsInfoServiceReentrantImpl(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Override
    public GoodsInfo getGoods() {
        try {
            readLock.lock();
            SleepTools.ms(5);
            return goodsInfo;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void setNum(int number) {
        try {
            writeLock.lock();
            SleepTools.ms(5);
            goodsInfo.changeNumber(number);
        } finally {
            writeLock.unlock();
        }
    }
}

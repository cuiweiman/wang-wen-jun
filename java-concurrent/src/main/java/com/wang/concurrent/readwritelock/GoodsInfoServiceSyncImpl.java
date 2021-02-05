package com.wang.concurrent.readwritelock;

import com.wang.concurrent.demo1.SleepTools;

/**
 * 内置锁 synchronized
 * 实现商品服务接口
 */
public class GoodsInfoServiceSyncImpl implements GoodsInfoService {
    private GoodsInfo goodsInfo;

    public GoodsInfoServiceSyncImpl(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Override
    public synchronized GoodsInfo getGoods() {
        SleepTools.ms(5);
        return this.goodsInfo;
    }

    @Override
    public synchronized void setNum(int number) {
        SleepTools.ms(5);
        goodsInfo.changeNumber(number);
    }
}

package com.wang.concurrent.readwritelock;

import com.wang.concurrent.demo1.SleepTools;

import java.util.Random;

/**
 * 业务应用类
 * 分别调用 synchronized 实现的锁，和 ReentrantReadWriteLock 实现的锁，比价其性能
 */
public class BusiApp {

    // 读写线程比例 读线程：写线程=10:1
    static final int readWriteRatio = 10;

    // 最少线程数
    static final int minThreadCount = 3;

    // 读线程
    private static class GetThread implements Runnable {
        private GoodsInfoService goodsInfoService;

        public GetThread(GoodsInfoService goodsInfoService) {
            this.goodsInfoService = goodsInfoService;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                goodsInfoService.getGoods();
            }
            System.out.println(Thread.currentThread().getName() + " 读取商品数量耗时： "
                    + (System.currentTimeMillis() - start));
        }
    }

    // 写线程
    private static class SetThread implements Runnable {
        private GoodsInfoService goodsInfoService;

        public SetThread(GoodsInfoService goodsInfoService) {
            this.goodsInfoService = goodsInfoService;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            Random r = new Random();
            for (int i = 0; i < 10; i++) {
                SleepTools.ms(50);
                goodsInfoService.setNum(r.nextInt(10));
            }
            System.out.println(Thread.currentThread().getName() + " 写入商品数量耗时： "
                    + (System.currentTimeMillis() - start));
        }
    }

    public static void main(String[] args) {
        GoodsInfo goodsInfo = new GoodsInfo("Cups", 100000, 10000);
        // 使用 synchronized 实现的 服务
        // GoodsInfoService goodsInfoService = new GoodsInfoServiceSyncImpl(goodsInfo);

        // 使用 ReentrantReadWriteLock 实现的服务
        GoodsInfoService goodsInfoService = new GoodsInfoServiceReentrantImpl(goodsInfo);

        // 每创建1个写线程，都创建10个读线程
        for (int i = 0; i < minThreadCount; i++) {
            Thread setT = new Thread(new SetThread(goodsInfoService));
            for (int j = 0; j < readWriteRatio; j++) {
                Thread getT = new Thread(new GetThread(goodsInfoService));
                getT.start();
            }
            SleepTools.ms(100);
            setT.start();
        }
    }

}

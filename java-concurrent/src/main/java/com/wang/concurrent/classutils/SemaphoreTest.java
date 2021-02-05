package com.wang.concurrent.classutils;

import com.wang.concurrent.demo1.SleepTools;

import java.sql.Connection;
import java.util.Random;

/**
 * 测试 模拟数据库 连接
 * 了解Semaphore的使用方法
 */
public class SemaphoreTest {

    private static SqlConnectedSemaphore dbPool = new SqlConnectedSemaphore();

    private static class BusinessThread extends Thread {
        @Override
        public void run() {
            Random r = new Random();
            long start = System.currentTimeMillis();
            try {
                Connection connection = dbPool.takeConnect();
                System.out.println("Thread_" + Thread.currentThread().getId()
                        + "_获取数据库连接共耗时【" + (System.currentTimeMillis() - start) + "ms】");

                SleepTools.ms(100 + r.nextInt(100));
                System.out.println("数据库操作完成，归还连接");

                dbPool.returnConnect(connection);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // 启动50个线程，获取数据库连接
        for (int i = 0; i < 50; i++) {
            Thread thread = new BusinessThread();
            thread.start();
        }
    }

}

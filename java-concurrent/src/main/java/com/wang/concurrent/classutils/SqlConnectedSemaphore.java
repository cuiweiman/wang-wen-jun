package com.wang.concurrent.classutils;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Semaphore 类 控
 * 配置 线程连接池
 * Semaphore 类 控制 访问共享资源的多线程个数
 * 模拟多线程 获取 数据库 连接资源
 */
public class SqlConnectedSemaphore {
    // 数据库连接池 有10个连接线程
    private final static int POOL_SIZE = 10;

    // 分别表示 当前可用的数据库连接； 已使用的数据库连接。
    private final Semaphore useful, useLess;


    /**
     * 初始状态，可用连接为 POOL_SIZE，已使用连接为 0
     */
    public SqlConnectedSemaphore() {
        this.useful = new Semaphore(POOL_SIZE);
        this.useLess = new Semaphore(0);
    }

    // 存放 数据库连接 的 容器
    private static LinkedList<Connection> pool = new LinkedList<>();

    // 初始化 连接池
    static {
        for (int i = 0; i < POOL_SIZE; i++) {
            pool.addLast(SqlConnection.fetchConnection());
        }
    }


    /**
     * 归还连接，连接池数量+1
     * 已使用连接数-1，可用连接数+1
     *
     * @param connection
     * @throws InterruptedException
     */
    public void returnConnect(Connection connection) throws InterruptedException {
        if (connection != null) {
            System.out.println("当前等待数据库连接线程的个数： " + useful.getQueueLength());
            System.out.println("当前已用连接数为：" + useLess.availablePermits()
                    + "\t当前有可用连接数：" + useful.availablePermits());
            useLess.acquire();
            synchronized (pool) {
                pool.addLast(connection);
            }
            useful.release();
        }
    }

    /**
     * 获取连接，连接池 数量-1
     * 可用的连接数-1；已使用连接数+1。
     *
     * @return
     * @throws InterruptedException
     */
    public Connection takeConnect() throws InterruptedException {
        useful.acquire();
        Connection conn;
        synchronized (pool) {
            conn = pool.removeFirst();
        }
        useLess.release();
        return conn;
    }

}

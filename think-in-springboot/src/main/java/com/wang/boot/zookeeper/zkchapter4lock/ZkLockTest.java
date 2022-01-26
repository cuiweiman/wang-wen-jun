package com.wang.boot.zookeeper.zkchapter4lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @description: zk Curator 框架实现分布式锁测试
 * @date: 2021/4/7 22:58
 * @author: wei·man cui
 */
public class ZkLockTest {
    public static void main(String[] args) {
        String hostPort = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(2000, 2000);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .retryPolicy(retryPolicy)
                .connectString(hostPort)
                .build();
        client.start();
        DistributeLockZooKeeper myLock = new DistributeLockZooKeeper(client, "/test", "lock-");
        while (true) {
            try {
                myLock.lock();
                Thread.sleep(5000);
            } catch (Exception e) {
                throw new RuntimeException(e.toString());
            } finally {
                myLock.unLock();
            }
        }
    }
}

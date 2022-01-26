package com.wang.zookeeper.distributelock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * [Curator实现分布式锁](https://curator.apache.org/index.html)
 * src/main/java/com/wang/boot/zookeeper/zkchapter4lock/DistributeLockZooKeeper.java Curator框架实现分布式锁测试。
 * <p>
 * 原生的 Java ZK API 分布式锁 存在的问题：
 * 1. 会话连接是异步的，需要自己去处理。比如使用 CountDownLatch
 * 2. Watch 需要重复注册，不然就不能生效
 * 3. 开发的复杂性还是比较高的
 * 4. 不支持多节点删除和创建，需要递归
 *
 * @description: ZK 原生 API 的 分布式锁存在很多问题
 * @author: cuiweiman
 * @date: 2022/1/26 16:43
 */
public class DistributedLock {

    private final ZooKeeper zk;
    // 锁 根节点 和 前缀
    private final String rootNode = "locks";
    private String subNode = "seq-";

    /**
     * 当前 client 等待的子节点
     */
    private String waitPath;

    /**
     * ZooKeeper 连接等待
     */
    private final CountDownLatch connectLatch = new CountDownLatch(1);
    /**
     * ZooKeeper 节点等待
     */
    private final CountDownLatch waitLatch = new CountDownLatch(1);
    /**
     * 当前 client 创建的子节点
     */
    private String currentNode;

    /**
     * 和 zk 服务建立连接，并创建根节点
     */
    public DistributedLock() throws IOException, InterruptedException, KeeperException {
        // 超时时间
        int sessionTimeout = 2000;
        // zookeeper server 列表
        String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        zk = new ZooKeeper(connectString, sessionTimeout, event -> {
            // 连接建立时, 打开 latch, 唤醒 wait 在该 latch 上的线程
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                connectLatch.countDown();
            }
            // 发生了 waitPath 的删除事件
            if (event.getType() == Watcher.Event.EventType.NodeDeleted && event.getPath().equals(waitPath)) {
                waitLatch.countDown();
            }
        });
        // 等待连接建立
        connectLatch.await();
        //获取根节点状态
        Stat stat = zk.exists("/" + rootNode, false);
        //如果根节点不存在，则创建根节点，根节点类型为永久节点
        if (stat == null) {
            System.out.println("根节点不存在");
            zk.create("/" + rootNode, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    /**
     * 加锁方法，在根节点下创建临时顺序节点
     */
    public void zkLock() {
        try {
            // 在根节点下创建临时顺序节点，返回值为创建的节点路径
            currentNode = zk.create("/" + rootNode + "/" + subNode, null,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            // wait 一小会, 让结果更清晰一些
            Thread.sleep(10);
            // 注意, 没有必要监听"/locks"的子节点的变化情况
            List<String> childrenNodes = zk.getChildren("/" + rootNode, false);
            // 列表中只有一个子节点, 那肯定就是 currentNode , 说明 client 获得锁。因此只处理 多个子节点的情况。
            if (childrenNodes.size() > 1) {
                //对根节点下的所有临时顺序节点进行从小到大排序
                Collections.sort(childrenNodes);
                //当前节点名称
                String thisNode = currentNode.substring(("/" + rootNode + "/").length());
                int index = childrenNodes.indexOf(thisNode);
                if (index == -1) {
                    // 创建的 znode 不在 锁根路径 下
                    System.out.println("数据异常");
                } else if (index > 0) {
                    //  thisNode 不在列表最前, 说明 存在客户端 释放 了一个节点（释放了一个分布式锁）
                    this.waitPath = "/" + rootNode + "/" + childrenNodes.get(index - 1);
                    // 在 waitPath 上注册监听器, 当 waitPath 被删除时, zookeeper 会回调监听器的 process 方法
                    zk.getData(waitPath, true, new Stat());
                    //进入等待锁状态
                    waitLatch.await();
                }
            }
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解锁方法
     */
    public void zkUnlock() {
        try {
            zk.delete(this.currentNode, -1);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }


}

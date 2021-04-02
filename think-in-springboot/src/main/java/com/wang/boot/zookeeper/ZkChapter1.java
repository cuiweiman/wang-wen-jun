package com.wang.boot.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @description: Zookeeper 常用 API 的使用
 * @author: wei·man cui
 * @date: 2021/4/2 17:13
 */
public class ZkChapter1 implements Watcher {
    private String CONNECT_STRING = "172.16.12.148:2181,172.16.12.149:2181,172.16.12.150:2181";
    private Integer SESSION_TIMEOUT = 2000;
    private ZooKeeper zookeeper;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 重写 Watcher 接口 方法
     *
     * @param watchedEvent 接收来自 Zookeeper 集群的事件
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        if (Objects.equals(watchedEvent.getState(), Event.KeeperState.SyncConnected)) {
            System.out.println("Watcher receive event");
            countDownLatch.countDown();
        }
    }

    /**
     * 建立 Zookeeper 连接
     *
     * @param host 主机
     * @throws IOException          异常
     * @throws InterruptedException 异常
     */
    public void connectZookeeper(String host) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper(host, SESSION_TIMEOUT, this);
        countDownLatch.await();
        System.out.println("Zookeeper connection success！");
    }

    /**
     * 创建节点
     *
     * @param path 路径
     * @param data 节点数据
     * @return 执行结果
     * @throws KeeperException      异常
     * @throws InterruptedException 异常
     */
    public String createNode(String path, String data) throws KeeperException, InterruptedException {
        return this.zookeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 获取路径下所有子节点
     *
     * @param path 路径
     * @return 子节点
     * @throws KeeperException      结果
     * @throws InterruptedException 结果
     */
    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        return zookeeper.getChildren(path, false);
    }

    /**
     * 获取节点上面的数据
     *
     * @param path  路径
     * @return 结果
     * @throws KeeperException      异常
     * @throws InterruptedException    异常
     */
    public String getData(String path) throws KeeperException, InterruptedException {
        byte[] data = zookeeper.getData(path, false, null);
        if (data == null) {
            return "";
        }
        return new String(data);
    }


    /**
     * 设置节点信息
     *
     * @param path 路径
     * @param data 数据
     * @return 结果
     * @throws KeeperException      异常
     * @throws InterruptedException 异常
     */
    public Stat setData(String path, String data) throws KeeperException, InterruptedException {
        return zookeeper.setData(path, data.getBytes(), -1);
    }

    /**
     * 删除节点
     *
     * @param path 路径
     * @throws InterruptedException 异常
     * @throws KeeperException      异常
     */
    public void deleteNode(String path) throws InterruptedException, KeeperException {
        zookeeper.delete(path, -1);
    }

    /**
     * 获取创建时间
     *
     * @param path 路径
     * @return 结果
     * @throws KeeperException      异常
     * @throws InterruptedException 异常
     */
    public String getCTime(String path) throws KeeperException, InterruptedException {
        Stat stat = zookeeper.exists(path, false);
        return String.valueOf(stat.getCtime());
    }

    /**
     * 获取某个路径下孩子的数量
     *
     * @param path 路径
     * @return 结果
     * @throws KeeperException      异常
     * @throws InterruptedException 异常
     */
    public Integer getChildrenNum(String path) throws KeeperException, InterruptedException {
        return zookeeper.getChildren(path, false).size();
    }

    /**
     * 关闭连接
     *
     * @throws InterruptedException 异常
     */
    public void closeConnection() throws InterruptedException {
        if (zookeeper != null) {
            zookeeper.close();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ZkChapter1 zkDemo = new ZkChapter1();
        zkDemo.connectZookeeper(zkDemo.CONNECT_STRING);
    }
}


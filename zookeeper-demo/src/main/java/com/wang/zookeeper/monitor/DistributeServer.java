package com.wang.zookeeper.monitor;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 传递 启动命令到 main 函数的 args 中：Edis configurations
 *
 * @description: 模拟 服务端的 上线和下线
 * @author: cuiweiman
 * @date: 2022/1/26 16:05
 */
public class DistributeServer {

    private ZooKeeper zk = null;

    /**
     * 创建到 zk 的客户端连接
     */
    public void getConnect() throws IOException {
        String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        int sessionTimeout = 2000;
        zk = new ZooKeeper(connectString, sessionTimeout, event -> {});
    }

    /**
     * 注册服务器，创建带序号的持久节点
     *
     * @param hostname 服务器名称
     */
    public void registerServer(String hostname) throws Exception {
        String parentNode = "/servers";
        String create = zk.create(parentNode + "/server", hostname.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostname + " is online " + create);
    }

    /**
     * 模拟业务功能
     */
    public void business(String hostname) throws Exception {
        System.out.println(hostname + " is working ...");
        Thread.currentThread().join();
    }

    public static void main(String[] args) throws Exception {
        // 1获取zk连接
        DistributeServer server = new DistributeServer();
        server.getConnect();
        // 2 利用 zk 连接注册服务器信息
        server.registerServer(args[0]);
        // 3 启动业务功能
        server.business(args[0]);
    }

}

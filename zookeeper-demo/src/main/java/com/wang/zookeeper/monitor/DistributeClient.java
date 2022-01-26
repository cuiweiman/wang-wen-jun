package com.wang.zookeeper.monitor;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 客户端 实时监听 服务端的 上线和下线状态
 * @author: cuiweiman
 * @date: 2022/1/26 16:01
 */
public class DistributeClient {
    private ZooKeeper zk = null;

    /**
     * 创建到 zk 的客户端连接
     *
     * @throws IOException 异常
     */
    public void getConnect() throws IOException {
        int sessionTimeout = 2000;
        String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        zk = new ZooKeeper(connectString, sessionTimeout, event -> {
            // 再次启动监听，已发生变化就会 收到通知
            try {
                getServerList();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 获取服务器列表信息
     *
     * @throws Exception 异常
     */
    public void getServerList() throws Exception {
        // 1 获取服务器子节点信息，并且对父节点进行监听
        String parentNode = "/servers";
        List<String> children = zk.getChildren(parentNode, true);
        // 2 存储服务器信息列表
        ArrayList<String> servers = new ArrayList<>();
        // 3 遍历所有节点，获取节点中的主机名称信息
        for (String child : children) {
            byte[] data = zk.getData(parentNode + "/" + child, false, null);
            servers.add(new String(data));
        }
        // 4 打印服务器列表信息
        System.out.println(servers);
    }

    /**
     * 模拟业务功能
     */
    public void business() throws Exception {
        System.out.println("client is working ...");
        Thread.currentThread().join();
    }

    public static void main(String[] args) throws Exception {
        // 1 获取zk连接
        DistributeClient client = new DistributeClient();
        client.getConnect();
        // 2 获取servers的子节点信息，从中获取服务器信息列表
        client.getServerList();
        // 3 业务进程启动
        client.business();
    }
}

package com.wang.zookeeper.clients;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * @description: Zookeeper 常用 API 的使用
 * @author: cuiweiman
 * @date: 2022/1/26 11:00
 */
public class BasicApi {

    private static final String CONNECT_STRING = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
    private static final Integer SESSION_TIMEOUT = 5000;

    private ZooKeeper zkClient;

    /**
     * 建立 Zookeeper 连接，获取 zk 客户端
     *
     * @throws IOException 异常
     */
    public void getZkClient() throws IOException {
        zkClient = new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new BasicWatcher());
    }

    /**
     * 创建节点
     * <p>
     * {@link  ZooDefs.Ids}
     * OPEN_ACL_UNSAFE：完全开放的ACL，任何连接的客户端都可以操作该属性 znode
     * CREATOR_ALL_ACL：只有创建者才有ACL权限
     * READ_ACL_UNSAFE：只能读取权限
     * <p>
     * {@link CreateMode}
     * PERSISTENT: 持久化目录节点, 会话结束存储数据不会丢失。
     * PERSISTENT_SEQUENTIAL: 顺序自动编号持久化目录节点, 存储数据不会丢失, 会根据当前已存在节点数自动加1, 然后返回给客户端已经创建成功的节点名。
     * EPHEMERAL:临时目录节点, 一旦创建这个节点当会话结束, 这个节点会被自动删除。
     * EPHEMERAL_SEQUENTIAL: 临时自动编号节点, 一旦创建这个节点,当回话结束, 节点会被删除, 并且根据当前已经存在的节点数自动加1, 然后返回给客户端已经成功创建的目录节点名 .
     * CONTAINER: 容器节点，用于Leader、Lock等特殊用途，当容器节点不存在任何子节点时，容器将成为服务器在将来某个时候删除的候选节点。
     * PERSISTENT_WITH_TTL: 带TTL（time-to-live，存活时间）的永久节点，节点在TTL时间之内没有得到更新并且没有孩子节点，就会被自动删除。
     * PERSISTENT_SEQUENTIAL_WITH_TTL: 带TTL（time-to-live，存活时间）和单调递增序号的永久节点，节点在TTL时间之内没有得到更新并且没有孩子节点，就会被自动删除。
     *
     * @param path 路径
     * @param data 节点数据
     * @return 执行结果
     * @throws KeeperException      异常
     * @throws InterruptedException 异常
     */
    public String createNode(String path, String data) throws InterruptedException, KeeperException {
        Stat exists = zkClient.exists(path, false);
        if (Objects.isNull(exists)) {
            return zkClient.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        return String.format("node %s exists", path);
    }

    /**
     * 获取路径下所有子节点
     *
     * @param path 路径
     * @return 子节点
     * @throws KeeperException      结果
     * @throws InterruptedException 结果
     */
    public List<String> getChildren(String path) throws InterruptedException, KeeperException {
        return zkClient.getChildren(path, false);
    }

    /**
     * 获取节点上面的数据
     *
     * @param path 路径
     * @return 结果
     * @throws KeeperException      异常
     * @throws InterruptedException 异常
     */
    public String getData(String path) throws InterruptedException, KeeperException {
        /*
        stat：描述节点状态信息的对象。stat对象中记录了一个节点的基本属性信息，例如节点创建时事务ID（cZxid）、
        最后一次修改的事务ID（mZxid）和节点数据内容的长度（dataLength）等。有时候，不仅需要获取节点最新的子节点列表，
        还要获取这个节点最新的节点状态信息。对于这种情况，可以将一个旧的stat变量传入API接口，该stat变量会在方法执行过程中，
        被服务端响应的新stat对象替换。
         */
        byte[] data = zkClient.getData(path, false, null);
        if (Objects.isNull(data)) {
            return StringUtils.EMPTY;
        }
        return new String(data);
    }

    /**
     * 设置节点数据
     *
     * @param path 路径
     * @param data 数据
     * @return 节点状态 {@link Stat}
     * @throws InterruptedException 异常
     * @throws KeeperException      异常
     */
    public Stat setData(String path, String data) throws InterruptedException, KeeperException {
        return zkClient.setData(path, data.getBytes(), -1);
    }


    /**
     * 删除节点
     *
     * @param path 路径
     * @throws InterruptedException 异常
     * @throws KeeperException      异常
     */
    public void delNode(String path) throws InterruptedException, KeeperException {
        zkClient.delete(path, -1);
    }

    /**
     * 迭代删除：N 叉树的 后序遍历
     *
     * @param path 路径 /path
     */
    public void delAllNode(String path) throws InterruptedException, KeeperException {
        Deque<String> allPath = new ArrayDeque<>();
        Deque<String> deque = new ArrayDeque<>();
        deque.push(path);
        while (!deque.isEmpty()) {
            String pop = deque.pop();
            allPath.push(pop);
            List<String> childPathList = zkClient.getChildren(pop, false);
            childPathList.forEach(childPath -> deque.push(pop.concat("/").concat(childPath)));
        }
        while (!allPath.isEmpty()) {
            String delPath = allPath.pop();
            // System.out.println(delPath);
            zkClient.delete(delPath, -1);
        }
    }


    /**
     * 获取节点创建时间 CTime
     *
     * @param path path
     * @return CTime
     * @throws InterruptedException 异常
     * @throws KeeperException      异常
     */
    public String getCtime(String path) throws InterruptedException, KeeperException {
        Stat stat = zkClient.exists(path, false);
        return String.valueOf(stat.getCtime());
    }

    /**
     * 关闭连接
     */
    public void closeConnect() throws InterruptedException {
        if (Objects.nonNull(zkClient)) {
            zkClient.close();
        }
    }

    public static void main(String[] args) {
        BasicApi zkDemo = new BasicApi();
        try {
            // 获取 Java Zookeeper 客户端
            zkDemo.getZkClient();
            // 创建节点，先删除 deleteall /basic-api-test
            zkDemo.createNode("/basic-api-test", "for test");
            zkDemo.createNode("/basic-api-test/child1", "children 1");
            zkDemo.createNode("/basic-api-test/child2", "children 2");
            // 获取子节点
            List<String> children = zkDemo.getChildren("/basic-api-test");
            children.forEach(System.out::println);
            // 获取节点数据
            String child2Data = zkDemo.getData("/basic-api-test/child2");
            System.out.println("获取节点数据 " + child2Data);
            // 设置节点数据
            Stat stat = zkDemo.setData("/basic-api-test/child1", "children 1 modify");
            System.out.println("child1 修改后的状态 ： " + stat.toString());
            // 节点创建时间
            String ctime = zkDemo.getCtime("/basic-api-test");
            System.out.println("basic-api-test 创建时间：" + ctime);
            // 删除节点
            // zkDemo.delNode("/basic-api-test");
            // 删除所有节点
            zkDemo.delAllNode("/basic-api-test");
            // 关闭  Java Zookeeper 客户端 与服务端的连接
            zkDemo.closeConnect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

package com.wang.boot.zookeeper.zkchapter3;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ExceptionUtil;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @description: ZK 分布式队列：生产者，消费者的实现
 * @author: wei·man cui
 * @date: 2021/4/6 16:09
 */
@Slf4j
public class DistributedQueue<T> {

    /**
     * 用于操作zookeeper集群
     */
    protected final CuratorFramework zkClient;

    /**
     * 代表根节点
     */
    protected final String root;

    /**
     * 顺序节点的名字
     */
    protected static final String NODE_NAME = "n_";

    public DistributedQueue(CuratorFramework zkClient, String root) {
        this.zkClient = zkClient;
        this.root = root;
    }

    /**
     * 获取队列大小
     *
     * @return 队列大小
     */
    public int size() {
        try {
            return zkClient.getChildren().forPath(root).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 判断队列是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        try {
            return CollectionUtils.isEmpty(zkClient.getChildren().forPath(root));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向队列提供数据,队列满的话,会阻塞等待,直到 start 标志位清除
     *
     * @param element 存放元素
     * @return 结果
     * @throws Exception 异常
     */
    public boolean offer(T element) throws Exception {
        // 构建数据节点的完整路径
        String nodeFullPath = root.concat("/").concat(NODE_NAME);
        try {
            // 创建 持久的有序节点
            zkClient.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(nodeFullPath);
        } catch (ZkNoNodeException e) {
            // root 节点不存在，创建后再 存放元素
            zkClient.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(root);
            offer(element);
        } catch (Exception e) {
            throw ExceptionUtil.convertToRuntimeException(e);
        }
        return true;
    }

    /**
     * 出队
     * 从队列取数据,当有start标志位时，开始取数据，全部取完数据后才删除start标志
     *
     * @return 结果
     */
    public T poll() {
        try {
            // 取出 root 节点下所有的 子节点
            final List<String> children = zkClient.getChildren().forPath(root);
            if (CollectionUtils.isEmpty(children)) {
                return null;
            }
            // 按照节点 后缀名 排序
            children.sort(Comparator.comparing(this::getNodeNumber));

            /*
             * 将队列中的元素做循环，然后构建完整的路径，在通过这个路径去读取数据
             */
            T node = null;
            for (String nodeName : children) {
                String nodeFullPath = root.concat("/").concat(nodeName);
                node = (T) byteToObject(zkClient.getData().forPath(nodeFullPath));
                zkClient.delete().forPath(nodeFullPath);
                if (Objects.nonNull(node)) {
                    break;
                }
            }
            return node;
        } catch (ZkNoNodeException e) {
            log.error("", e);
            return null;
        } catch (Exception e) {
            throw ExceptionUtil.convertToRuntimeException(e);
        }
    }

    /**
     * 截取节点的数字的方法
     *
     * @param str 节点路径
     * @return 节点后缀的数字
     */
    private String getNodeNumber(String str) {
        int index = str.lastIndexOf(NODE_NAME);
        if (index >= 0) {
            index += NODE_NAME.length();
            return index <= str.length() ? str.substring(index) : "";
        }
        return str;

    }

    /**
     * bytes 转 object
     *
     * @param bytes 字节数组
     * @return 结果
     */
    private Object byteToObject(byte[] bytes) {
        Object obj = null;
        try (ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
             ObjectInputStream oi = new ObjectInputStream(bi)) {
            obj = oi.readObject();
        } catch (Exception e) {
            log.error("translation" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }


}

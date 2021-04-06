package com.wang.boot.zookeeper.zkchapter3;

import jodd.exception.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

/**
 * @description: ZK 分布式队列：同步队列的实现
 * @author: wei·man cui
 * @date: 2021/4/6 16:09
 */
@Slf4j
public class DistributedQueue<T> {

    /**
     * 根节点
     */
    protected final String root;
    protected final ZooKeeper zooKeeper;
    private int queueSize;
    private String startPath = "/queue/start";

    /**
     * 顺序节点的名字
     */
    protected static final String Node_Name = "n_";

    public DistributedQueue(String root, ZooKeeper zooKeeper, int queueSize) {
        this.root = root;
        this.zooKeeper = zooKeeper;
        this.queueSize = queueSize;
        init();
    }

    private void init() {
        try {
            // 初始化
            Stat stat = zooKeeper.exists(root, false);
            if (Objects.isNull(stat)) {
                zooKeeper.create(root, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            zooKeeper.delete(startPath, -1);
        } catch (KeeperException | InterruptedException e) {
            log.error("create rootPath error", e);
        }
    }

    /**
     * 获取队列大小
     *
     * @return 队列大小
     */
    public int size() throws KeeperException, InterruptedException {
        return zooKeeper.getChildren(root, false).size();
    }

    /**
     * 判断队列是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() throws KeeperException, InterruptedException {
        return CollectionUtils.isEmpty(zooKeeper.getChildren(root, false));
    }

    /**
     * bytes 转 object
     *
     * @param bytes 数组
     * @return 结果
     */
    private Object byteToObject(byte[] bytes) {
        Object obj = null;
        try {
            // ByteArray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            log.error("translation" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * Object 转byte
     *
     * @param obj 对象
     * @return 字节数组
     */
    private byte[] objectToByte(java.lang.Object obj) {
        byte[] bytes = null;
        try {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        } catch (Exception e) {
            log.error("translation" + e.getMessage());
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 向队列提供数据,队列满的话会阻塞等待直到start标志位清除
     *
     * @param element
     * @return
     * @throws Exception
     */
    // public boolean offer(T element) throws Exception {
    //     // 构建数据节点的完整路径
    //     String nodeFullPath = root.concat("/").concat(Node_NAME);
    //     try {
    //         if (queueSize > size()) {
    //             // 创建持久的节点，写入数据
    //             zooKeeper.create(nodeFullPath, ObjectToByte(element), ZooDefs.Ids.OPEN_ACL_UNSAFE,
    //                     CreateMode.PERSISTENT);
    //             // 再判断一下队列是否满
    //             if (queueSize > size()) {
    //                 zooKeeper.delete(startPath, -1); // 确保不存在
    //             } else {
    //                 zooKeeper.create(startPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    //             }
    //         } else {
    //             // 创建队列满的标记
    //             if (zooKeeper.exists(startPath, false) != null) {
    //                 zooKeeper.create(startPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    //             }
    //
    //             final CountDownLatch latch = new CountDownLatch(1);
    //             final Watcher previousListener = new Watcher() {
    //                 public void process(WatchedEvent event) {
    //                     if (event.getType() == EventType.NodeDeleted) {
    //                         latch.countDown();
    //                     }
    //                 }
    //             };
    //
    //             // 如果节点不存在会出现异常
    //             zooKeeper.exists(startPath, previousListener);
    //             latch.await();
    //             offer(element);
    //
    //         }
    //     } catch (ZkNoNodeException e) {
    //         logger.error("", e);
    //     } catch (Exception e) {
    //         throw ExceptionUtil.convertToRuntimeException(e);
    //     }
    //     return true;
    // }

    /**
     * 从队列取数据,当有start标志位时，开始取数据，全部取完数据后才删除start标志
     *
     * @return
     * @throws Exception
     */
    // @SuppressWarnings("unchecked")
    // public T poll() throws Exception {
    //
    //     try {
    //         // 队列还没满
    //         if (zooKeeper.exists(startPath, false) == null) {
    //             final CountDownLatch latch = new CountDownLatch(1);
    //             final Watcher previousListener = new Watcher() {
    //                 public void process(WatchedEvent event) {
    //                     if (event.getType() == EventType.NodeCreated) {
    //                         latch.countDown();
    //                     }
    //                 }
    //             };
    //
    //             // 如果节点不存在会出现异常
    //             zooKeeper.exists(startPath, previousListener);
    //
    //             // 如果节点不存在会出现异常
    //             latch.await();
    //         }
    //
    //         List<String> list = zooKeeper.getChildren(root, false);
    //         if (list.size() == 0) {
    //             return null;
    //         }
    //         // 将队列按照由小到大的顺序排序
    //         Collections.sort(list, new Comparator<String>() {
    //             public int compare(String lhs, String rhs) {
    //                 return getNodeNumber(lhs, Node_NAME).compareTo(getNodeNumber(rhs, Node_NAME));
    //             }
    //         });
    //
    //         /**
    //          * 将队列中的元素做循环，然后构建完整的路径，在通过这个路径去读取数据
    //          */
    //         for (String nodeName : list) {
    //             String nodeFullPath = root.concat("/").concat(nodeName);
    //             try {
    //                 T node = (T) byteToObject(zooKeeper.getData(nodeFullPath, false, null));
    //                 zooKeeper.delete(nodeFullPath, -1);
    //                 return node;
    //             } catch (ZkNoNodeException e) {
    //                 log.error("", e);
    //             }
    //         }
    //         return null;
    //     } catch (Exception e) {
    //         throw ExceptionUtil.convertToRuntimeException(e);
    //     }
    //
    // }

    /**
     * 截取节点的数字的方法
     *
     * @param str
     * @param nodeName
     * @return
     */
    private String getNodeNumber(String str, String nodeName) {
        int index = str.lastIndexOf(nodeName);
        if (index >= 0) {
            index += Node_Name.length();
            return index <= str.length() ? str.substring(index) : "";
        }
        return str;

    }
}

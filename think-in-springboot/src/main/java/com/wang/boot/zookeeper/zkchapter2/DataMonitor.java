package com.wang.boot.zookeeper.zkchapter2;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Arrays;
import java.util.Objects;

/**
 * 执行 异步 API 时，处理 服务端的响应
 *
 * @description: 接收 ZK 节点修改的 回调通知（接收数据变化通知）
 * @date: 2021/4/5 23:25
 * @author: wei·man cui
 */
public class DataMonitor implements AsyncCallback.StatCallback {

    private ZooKeeper zk;
    private String zNode;
    boolean dead;
    private DataMonitorListener listener;
    private byte prevData[];

    public DataMonitor(ZooKeeper zk, String zNode, DataMonitorListener listener) {
        this.zk = zk;
        this.zNode = zNode;
        this.listener = listener;
        zk.exists(zNode, true, this, null);
    }

    public void handler(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType() == Watcher.Event.EventType.None) {
            switch (event.getState()) {
                case SyncConnected:
                    break;
                case Expired:
                    dead = true;
                    listener.closing(KeeperException.Code.SESSIONEXPIRED.intValue());
                    break;
                default:
                    break;
            }
        } else {
            if (Objects.nonNull(path) && Objects.equals(path, zNode)) {
                zk.exists(zNode, true, this, null);
            }
        }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        boolean exists = false;
        switch (i) {
            case KeeperException.Code
                    .Ok:
                exists = true;
                break;
            case KeeperException.Code
                    .NoNode:
                exists = false;
                break;
            case KeeperException.Code
                    .SessionExpired:
            case KeeperException.Code
                    .NoAuth:
                dead = true;
                listener.closing(i);
                break;
            default:
                zk.exists(zNode, true, this, null);
                break;
        }
        byte[] b = null;
        if (exists) {
            try {
                b = zk.getData(zNode, false, null);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        boolean flag = (Objects.isNull(b) && b != prevData)
                || (Objects.nonNull(b) && !Arrays.equals(prevData, b));
        if (flag) {
            listener.exists(b);
            prevData = b;
        }
    }

    public interface DataMonitorListener {
        /**
         * ZNode 存在状态是否 改变
         *
         * @param data 数据
         */
        void exists(byte[] data);

        /**
         * ZK 会话关闭时间
         *
         * @param rc 参数
         */
        void closing(int rc);
    }
}

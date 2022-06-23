package com.wang.zookeeper.watch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2022/3/1 23:41
 */
public class ZkWatch implements Watcher {

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected && watchedEvent.getType() == Event.EventType.NodeDataChanged) {
            // 收到的事件所发生的节点路径
            System.out.println(watchedEvent.getPath());
            // 收到的事件的类型
            System.out.println(watchedEvent.getType());
            // 收到事件后，我们的处理逻辑
            System.out.println("节点数据发生了变化.....");
        } else if (watchedEvent.getState() == Event.KeeperState.SyncConnected && watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
            System.out.println("子节点变化了......");
        }
    }
}

package com.wang.zookeeper.clients;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @description: 客户端监听
 * @author: cuiweiman
 * @date: 2022/1/26 15:09
 */
@Slf4j
public class BasicWatcher implements Watcher {

    @Override
    public void process(WatchedEvent watchedEvent) {
        log.debug(" receive event : {} ", watchedEvent.getType().name());
    }
}

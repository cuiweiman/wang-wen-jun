package com.wang.guava.eventbus.monitor;

import com.google.common.eventbus.EventBus;

/**
 * @description: 监听文件目录 中的文件和文件夹是否发生改变
 * @date: 2020/8/10 23:12
 * @author: wei·man cui
 */
public class MonitorClient {
    public static void main(String[] args) throws Exception {
        final EventBus bus = new EventBus();
        bus.register(new FileChangeListener());
        String path = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava";
        TargetMonitor monitor = new DirectoryTargetMonitor(bus, path);
        monitor.startMonitor();
    }
}

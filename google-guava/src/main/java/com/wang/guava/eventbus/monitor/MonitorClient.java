package com.wang.guava.eventbus.monitor;

import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            try {
                monitor.stopMonitor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 2, TimeUnit.MINUTES);
        executorService.shutdown();
        // 启动后，只能通过其他线程来关闭监听。这里开启了一个 定时线程 来关闭。
        monitor.startMonitor();
    }
}

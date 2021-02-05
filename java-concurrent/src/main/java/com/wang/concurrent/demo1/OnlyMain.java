package com.wang.concurrent.demo1;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class OnlyMain {
    public static void main(String[] args) {
        // 获取 虚拟机 线程管理类
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 获取 所有线程先关信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("{" + threadInfo.getThreadId() + "} " + threadInfo.getThreadName());
        }
    }
}

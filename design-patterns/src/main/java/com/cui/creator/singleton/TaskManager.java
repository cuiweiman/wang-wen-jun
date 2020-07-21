package com.cui.creator.singleton;

/**
 * @description: 单例demo  任务管理器单例
 * @author: wei·man cui
 * @date: 2020/7/9 16:23
 */
public class TaskManager {

    public static TaskManager tm = null;

    public static TaskManager getInstance() {
        if (tm == null) {
            tm = new TaskManager();
        }
        return tm;
    }


}

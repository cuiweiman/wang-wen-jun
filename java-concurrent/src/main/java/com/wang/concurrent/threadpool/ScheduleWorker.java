package com.wang.concurrent.threadpool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 定时任务 的 工作类
 */
public class ScheduleWorker implements Runnable {

    // 普通任务类型
    public final static int Normal = 0;

    // 会抛出异常的任务类型
    public final static int HasException = -1;

    // 抛出异常但会捕捉的任务类型
    public final static int ProcessException = 1;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private int taskType;

    public ScheduleWorker(int taskType) {
        this.taskType = taskType;
    }

    @Override
    public void run() {
        if (taskType == HasException) {
            System.out.println(formatter.format(LocalDateTime.now()) + " Exception made ……");
            throw new RuntimeException("HasException happen");
        } else if (taskType == ProcessException) {
            try {
                System.out.println(formatter.format(LocalDateTime.now()) + " Exception made but catch ……");
            } catch (Exception e) {
                throw new RuntimeException("HasException has been caught");
            }
        } else {
            System.out.println("Normal …… " + formatter.format(LocalDateTime.now()));
        }
    }
}

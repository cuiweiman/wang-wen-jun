package com.wang.concurrent.threadpool;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestScheduledWorker {

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(1);

        /**
         * 延时1ms执行，每隔3ms自行一次
         * Normal：无异常
         * HasException：会抛出异常的任务类型
         * ProcessException：抛出异常但会捕捉的任务类型
         */
        schedule.scheduleWithFixedDelay(
                // new ScheduleWorker(ScheduleWorker.Normal),
                new ScheduleWorker(ScheduleWorker.ProcessException),
                // new ScheduleWorker(ScheduleWorker.HasException),
                1000,
                3000,
                TimeUnit.MILLISECONDS);
    }

}

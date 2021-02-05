package com.wang.concurrent.practice;

import com.wang.concurrent.demo1.SleepTools;
import com.wang.concurrent.practice.vo.TaskResultVO;

import java.util.List;
import java.util.Random;

/**
 * @author weiman cui
 * @date 2020/5/21 22:04
 */
public class AppTest {
    private final static String JOB_NAME = "计算数值";
    private final static int JOB_LENGTH = 1000;

    // 查看任务进度的线程
    private static class QueryResult implements Runnable {
        private PendingJobPool pool;

        public QueryResult(PendingJobPool pool) {
            super();
            this.pool = pool;
        }

        @Override
        public void run() {
            int i = 0;
            while (i < 350) {
                List<TaskResultVO<String>> taskDetail = pool.getTaskDetail(JOB_NAME);
                if (!taskDetail.isEmpty()) {
                    System.out.println(pool.getTaskProcess(JOB_NAME));
                    System.out.println(taskDetail);
                }
                SleepTools.ms(100);
                i++;
            }
        }
    }

    public static void main(String[] args) {
        MyTask myTask = new MyTask();
        // 获取 框架实例，注册JOB
        PendingJobPool pool = PendingJobPool.getInstance();
        pool.registerJob(JOB_NAME, JOB_LENGTH, myTask, 1000 * 5);
        Random r = new Random();
        for (int i = 0; i < JOB_LENGTH; i++) {
            // 依次推入Task
            pool.putTask(JOB_NAME, r.nextInt(1000));
        }
        Thread t = new Thread(new QueryResult(pool));
        t.start();
    }
}

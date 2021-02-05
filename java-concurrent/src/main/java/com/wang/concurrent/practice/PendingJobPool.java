package com.wang.concurrent.practice;

import com.wang.concurrent.practice.vo.JobInfo;
import com.wang.concurrent.practice.vo.TaskResultEnum;
import com.wang.concurrent.practice.vo.TaskResultVO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 待处理任务池，并发任务执行框架
 */
public class PendingJobPool {

    // 线程池数量===>处理器的数量
    private static final int THREAD_COUNTS = Runtime.getRuntime().availableProcessors();

    // 存放 提交任务的 队列
    private static BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(5000);

    // 创建自定义线程池，固定大小、且有界队列
    private static ExecutorService taskExecutor =
            new ThreadPoolExecutor(THREAD_COUNTS, THREAD_COUNTS, 60, TimeUnit.SECONDS,
                    taskQueue);

    // job的存放容器
    private static Map<String, JobInfo<?>> jobInfoMap = new ConcurrentHashMap<>();

    private static CheckJobProcessor checkJobProcessor = CheckJobProcessor.getInstance();

    public static Map<String, JobInfo<?>> getMap() {
        return jobInfoMap;
    }

    // 单例模式
    private PendingJobPool() {
    }

    private static class JobPoolHolder {
        public static PendingJobPool pool = new PendingJobPool();
    }

    /**
     * 饿汉式 延迟占位法 创建单例。使框架使用者可以获取到框架实例
     *
     * @return
     */
    public static PendingJobPool getInstance() {
        return JobPoolHolder.pool;
    }

    /**
     * 调用者注册JobInfo
     */
    public <R> void registerJob(String jobName, int jobLength,
                                ITaskProcessor<?, ?> taskProcessor, long expireTime) {
        JobInfo<R> jobInfo = new JobInfo<R>(jobName, jobLength, taskProcessor, expireTime);
        // Map.putIfAbsent(),若map中有则返回出来，若不存在则放入容器并返回null
        if (jobInfoMap.putIfAbsent(jobName, jobInfo) != null) {
            throw new RuntimeException(jobName + " 已经注册！");
        }
    }

    /**
     * 根据工作名，查询工作
     */
    private <R> JobInfo<R> getJob(String jobName) {
        JobInfo<R> jobInfo = (JobInfo<R>) jobInfoMap.get(jobName);
        if (jobInfo == null) {
            throw new RuntimeException(jobName + " 是非法任务！");
        }
        return jobInfo;
    }

    /**
     * 调用者将 工作任务 提交给线程池中的线程去执行
     */
    public <T, R> void putTask(String jobName, T t) {
        JobInfo<R> jobInfo = getJob(jobName);
        PendingTask<T, R> task = new PendingTask<>(jobInfo, t);
        taskExecutor.execute(task);
    }

    /**
     * 获取每个任务的处理详情/执行情况
     */
    public <R> List<TaskResultVO<R>> getTaskDetail(String jobName) {
        JobInfo<R> jobInfo = getJob(jobName);
        return jobInfo.getTaskDetail();
    }

    /**
     * 获取每个任务的处理 进度
     */
    public <R> String getTaskProcess(String jobName) {
        JobInfo<R> jobInfo = getJob(jobName);
        return jobInfo.getTotalProcess();
    }

    /**
     * 对工作中的任务进行包装，提交给线程池使用，并处理任务的结果，写入缓存以供查询
     */
    private static class PendingTask<T, R> implements Runnable {
        private JobInfo<R> jobInfo;
        private T processorData;

        public PendingTask(JobInfo<R> jobInfo, T processorData) {
            this.jobInfo = jobInfo;
            this.processorData = processorData;
        }

        @Override
        public void run() {
            R r = null;
            ITaskProcessor<T, R> taskProcessor = (ITaskProcessor<T, R>) jobInfo.getTaskProcessor();
            TaskResultVO<R> resultVO = null;
            try {
                // 调用 业务人员实现的 具体方法
                resultVO = taskProcessor.taskExecutor(processorData);
                // 防止 业务人员处理不当
                if (resultVO == null) {
                    resultVO = new TaskResultVO<>(TaskResultEnum.EXCEPTION, r, "result is null!");
                }
                if (resultVO.getResultEnum() == null) {
                    if (resultVO.getReason() == null) {
                        resultVO = new TaskResultVO<>(TaskResultEnum.EXCEPTION, r, "reason is null!");
                    } else {
                        resultVO = new TaskResultVO<>(TaskResultEnum.EXCEPTION, r,
                                "result is null! but reason: " + resultVO.getReason());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultVO = new TaskResultVO<>(TaskResultEnum.EXCEPTION, r, e.getMessage());
            } finally {
                jobInfo.addTaskResult(resultVO, checkJobProcessor);
            }
        }
    }
}

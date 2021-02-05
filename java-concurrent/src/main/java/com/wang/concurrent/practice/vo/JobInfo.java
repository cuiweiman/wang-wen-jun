package com.wang.concurrent.practice.vo;

import com.wang.concurrent.practice.CheckJobProcessor;
import com.wang.concurrent.practice.ITaskProcessor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 提交给框架执行的工作实体类
 */
public class JobInfo<R> {

    // 区分唯一的工作
    private final String jobName;

    //工作的任务个数
    private final int jobLength;

    // 处理 这个工作任务的处理器
    private final ITaskProcessor<?, ?> taskProcessor;

    // 任务的过期时间
    private final long expireTime;

    // 处理任务成功的 计数器
    private AtomicInteger successCount;

    // 处理任务的 总计数
    private AtomicInteger taskProcessorCount;

    // 保存每个任务的执行结果
    private LinkedBlockingDeque<TaskResultVO<R>> taskDetailQueue;

    public JobInfo(String jobName, int jobLength, ITaskProcessor<?, ?> taskProcessor, long expireTime) {
        this.jobName = jobName;
        this.jobLength = jobLength;
        this.taskProcessor = taskProcessor;
        this.expireTime = expireTime;
        this.successCount = new AtomicInteger(0);
        this.taskProcessorCount = new AtomicInteger(0);
        this.taskDetailQueue = new LinkedBlockingDeque<>(jobLength);
    }

    /**
     * 对外提供 查询进度 的方法，获取成功的任务数
     *
     * @return
     */
    public int getSuccessCount() {
        return successCount.get();
    }

    /**
     * 对外提供 查询进度 的方法，获取已执行的任务数
     *
     * @return
     */
    public int getTaskProcessorCount() {
        return taskProcessorCount.get();
    }

    public ITaskProcessor<?, ?> getTaskProcessor() {
        return taskProcessor;
    }

    /**
     * 查询 执行失败的任务数
     *
     * @return
     */
    public int getFailedCount() {
        return taskProcessorCount.get() - successCount.get();
    }

    /**
     * 查询 执行进度： Success[5]/Current[8] Total[10]
     * 总计10个任务，已执行8个，成功5个
     *
     * @return
     */
    public String getTotalProcess() {
        return "Success[" + successCount.get() + "]/Current[" + taskProcessorCount.get() + "]"
                + "] Total[" + jobLength + "]";

    }

    /**
     * 对外提供 查询每个任务 执行结果 的方法
     * 不需要对方法加锁的原因：业务角度而言，保证最终结果一直性即可
     *
     * @return
     */
    public List<TaskResultVO<R>> getTaskDetail() {
        List<TaskResultVO<R>> taskResultVOList = new LinkedList<>();
        TaskResultVO<R> taskResultVO;
        // 从阻塞队列 头部 取出任务结果。新增任务结果时从尾部加入。避免拿和放的冲突
        while ((taskResultVO = taskDetailQueue.pollFirst()) != null) {
            taskResultVOList.add(taskResultVO);
        }
        return taskResultVOList;
    }

    /**
     * 从尾部 向阻塞队列中 加入 任务的执行结果
     *
     * @param resultVO
     */
    public void addTaskResult(TaskResultVO<R> resultVO, CheckJobProcessor checkJobProcessor) {
        if (TaskResultEnum.SUCCESS.equals(resultVO.getResultEnum())) {
            successCount.incrementAndGet();
        }
        // 不论成功还是失败，队列中都需要存储 执行结果，并且已执行计数器+1.
        taskDetailQueue.addLast(resultVO);
        taskProcessorCount.incrementAndGet();
        if (taskProcessorCount.get() == jobLength) {
            checkJobProcessor.putJob(jobName, expireTime);
        }
    }

}

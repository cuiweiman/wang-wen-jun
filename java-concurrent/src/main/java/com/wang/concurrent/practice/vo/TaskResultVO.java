package com.wang.concurrent.practice.vo;

import lombok.Getter;

/**
 * 任务处理结果 实体类
 *
 * @author weiman cui
 * @date 2020/5/19 15:45
 */
@Getter
public class TaskResultVO<R> {
    private final TaskResultEnum resultEnum;

    /**
     * 方法的业务结果数据
     */
    private final R respData;

    /**
     * 失败原因
     */
    private final String reason;

    public TaskResultVO(TaskResultEnum resultEnum, R respData) {
        this.resultEnum = resultEnum;
        this.respData = respData;
        this.reason = "SUCCESS";
    }

    public TaskResultVO(TaskResultEnum resultEnum, R respData, String reason) {
        this.resultEnum = resultEnum;
        this.respData = respData;
        this.reason = reason;
    }
}

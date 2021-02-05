package com.wang.concurrent.practice;

import com.wang.concurrent.demo1.SleepTools;
import com.wang.concurrent.practice.vo.TaskResultEnum;
import com.wang.concurrent.practice.vo.TaskResultVO;

import java.util.Random;

/**
 * @author weiman cui
 * @date 2020/5/21 22:01
 */
public class MyTask implements ITaskProcessor<Integer, Integer> {
    @Override
    public TaskResultVO<Integer> taskExecutor(Integer data) {
        Random r = new Random();
        int flag = r.nextInt(500);
        SleepTools.ms(flag);
        if (flag <= 300) {
            Integer returnValue = data.intValue();
            return new TaskResultVO<Integer>(TaskResultEnum.SUCCESS, returnValue);
        } else if (flag > 301 && flag <= 400) {
            return new TaskResultVO<Integer>(TaskResultEnum.FAILURE, -1, "FAILURE");
        } else {
            try {
                throw new RuntimeException("异常发生了！");
            } catch (Exception e) {
                return new TaskResultVO<>(TaskResultEnum.EXCEPTION, -1, e.getMessage());
            }
        }

    }
}

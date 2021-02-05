package com.wang.concurrent.practice;

import com.wang.concurrent.practice.vo.TaskResultVO;

/**
 * 要求框架使用者实现的接口，因为任务的性质在调用时才知道，所以传参和返回值都使用泛型
 *
 * @author weiman cui
 * @date 2020/5/19 15:40
 */
public interface ITaskProcessor<T, R> {

    TaskResultVO<R> taskExecutor(T data);

}


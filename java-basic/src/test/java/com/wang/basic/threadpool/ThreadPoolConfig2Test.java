package com.wang.basic.threadpool;

import com.wang.basic.JavaBasicApplicationTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2020/10/29 9:27
 * @see ThreadPoolConfig2 最大线程数=10，阻塞队列最大容量是 20,
 */
public class ThreadPoolConfig2Test extends JavaBasicApplicationTest {

    @Resource
    private Executor asyncExecutor;

    @Test
    public void test() {
        // 任务过多，会根据 拒绝策略 进行 任务拒绝
        for (int i = 0; i < 50; i++) {
            asyncExecutor.execute(new RunnableTask());
        }
    }


}

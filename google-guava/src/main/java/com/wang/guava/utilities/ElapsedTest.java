package com.wang.guava.utilities;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @description: Elapsed 打印日志
 * @date: 2020/7/30 22:08
 * @author: wei·man cui
 */
public class ElapsedTest {

    @Test
    public void testProcess() throws InterruptedException {
        process("123456");
    }

    public void process(String orderNo) throws InterruptedException {
        System.out.printf("start process the order %s\n", orderNo);
        long currentTime = System.currentTimeMillis();
        TimeUnit.SECONDS.sleep(1);
        System.out.printf("The orderNo %s process successful" +
                "and elapsed %d ns.\n", orderNo, System.currentTimeMillis() - currentTime);
    }

}

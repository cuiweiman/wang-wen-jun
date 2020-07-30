package com.wang.guava.utilities;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @description: StopWatch 计时器
 * @date: 2020/7/30 22:16
 * @author: wei·man cui
 */
@Slf4j
public class StopWatchTest {
    @Test
    public void testProcess() throws InterruptedException {
        process("123456");
    }

    public void process(String orderNo) throws InterruptedException {
        log.info("start process the order {}", orderNo);
        Stopwatch stopwatch = Stopwatch.createStarted();
        TimeUnit.SECONDS.sleep(1);
        // log.info("The orderNo {} process successful and elapsed {}.", orderNo, stopwatch.stop());
        log.info("The orderNo {} process successful and elapsed {}.", orderNo, stopwatch.stop().elapsed(TimeUnit.MINUTES));
    }


}

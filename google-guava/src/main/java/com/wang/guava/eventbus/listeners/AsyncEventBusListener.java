package com.wang.guava.eventbus.listeners;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @description: 用于测试 异步事件总线的  订阅者
 * @author: wei·man cui
 * @date: 2020/8/17 9:44
 */
@Slf4j
public class AsyncEventBusListener {

    @Subscribe
    private void doAction(final String event) {
        if (log.isInfoEnabled()) {
            log.info("Received event [{}] and will take a action.", event);
        }
    }

    @Subscribe
    private void doAction1(final String event) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (log.isInfoEnabled()) {
            log.info("Received event [{}] and will take a action1.", event);
        }
    }

    @Subscribe
    private void doAction2(final String event) {
        if (log.isInfoEnabled()) {
            log.info("Received event [{}] and will take a action2.", event);
        }
    }

}

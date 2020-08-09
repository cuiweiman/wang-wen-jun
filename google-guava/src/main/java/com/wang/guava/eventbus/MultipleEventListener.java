package com.wang.guava.eventbus;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 可以接收多种类型 事件 的 Listener.类型必须是包装类。
 * @date: 2020/8/9 16:18
 * @author: wei·man cui
 */
@Slf4j
public class MultipleEventListener {

    @Subscribe
    private void task1(String event) {
        if (log.isInfoEnabled()) {
            log.info("【task1】Received event [{}] and will take a action.", event);
        }
    }

    @Subscribe
    private void task2(String event) {
        if (log.isInfoEnabled()) {
            log.info("【task2】Received event [{}] and will take a action.", event);
        }
    }

    @Subscribe
    private void integerTask(Integer event) {
        if (log.isInfoEnabled()) {
            log.info("【intTask】Received event [{}] and will take a action.", event);
        }
    }

}

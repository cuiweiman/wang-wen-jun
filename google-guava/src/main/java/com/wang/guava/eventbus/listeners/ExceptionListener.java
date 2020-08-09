package com.wang.guava.eventbus.listeners;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 异常处理 Demo。
 * Listener其中一个方法异常，不会影响其他方法的执行。但是默认是被Subscribe捕获的，无法自定义
 * 因此 我们在 ExceptionEventBus 总线类中自定义一个异常，来进行捕获
 * @date: 2020/8/9 23:29
 * @author: wei·man cui
 */
@Slf4j
public class ExceptionListener {

    @Subscribe
    public void m1(String event) {
        log.info("【m1】{}", event);
    }

    @Subscribe
    public void m2(String event) {
        log.info("【m2】{}", event);
    }

    @Subscribe
    public void m3(String event) {
        log.info("【m3】{}", event);
        throw new RuntimeException();
    }

}

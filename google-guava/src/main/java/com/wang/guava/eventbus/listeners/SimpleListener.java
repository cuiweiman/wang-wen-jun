package com.wang.guava.eventbus.listeners;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: EventBus 简单示例 Listener.需要注册到EventBus中
 * @date: 2020/8/9 16:11
 * @author: wei·man cui
 */
@Slf4j
public class SimpleListener {

    /**
     * @param event String类型的事件
     * @Subscribe 注解：事件源发送了一个String类型的event 到 EventBus，
     * 会将该event交由 被Subscribe注释的方法。
     * @desc 不仅需要使用@Subscribe注解修饰，还需要将方法注册到EventBus。
     */
    @Subscribe
    private void doAction(final String event) {
        if (log.isInfoEnabled()) {
            log.info("Received event [{}] and will take a action.", event);
        }
    }

}

package com.wang.guava.eventbus.listeners;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: Listener之间的继承关系：
 * 可以发现，当有事件发送到EventBus中时，Listener的子类到父类都会执行
 * @date: 2020/8/9 16:24
 * @author: wei·man cui
 */
@Slf4j
public class BaseListener extends AbstractListener {

    @Subscribe
    public void baseTask(String event) {
        if (log.isInfoEnabled()) {
            log.info("The event [{}] will be handle by {}.{}.", this.getClass().getSimpleName(), "baseTask");
        }
    }
}

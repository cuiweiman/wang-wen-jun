package com.wang.guava.eventbus.listeners;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

/**
 * @description: DeadEvent 实例
 * @date: 2020/8/9 23:43
 * @author: wei·man cui
 */
public class DeadEventListener {

    @Subscribe
    public void handle(DeadEvent event) {
        System.out.println("【事件来源】" + event.getSource());
        System.out.println("【事件信息】" + event.getEvent());
    }

}

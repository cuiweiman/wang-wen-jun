package com.wang.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.wang.guava.eventbus.service.QueryService;
import com.wang.guava.eventbus.service.RequestQueryHandler;

/**
 * <p>
 * 1. EventBus作为中间事件传递；
 * 2. QueryService提交事件给EventBus，并从EventBus接收反馈；
 * 3. RequestQueryHandler从EventBus接收事件，处理后再提交给EventBus；
 * 从而通过EventBus，将 QueryService 和 RequestQueryHandler 的交互进行了解耦。
 * 4. QueryService 和 RequestQueryHandler 即是 事件发布者，也是事件订阅者。
 * </p>
 *
 * @description: 事件解耦 Demo。
 * @date: 2020/8/9 23:59
 * @author: wei·man cui
 */
public class ComEachOtherEventBus {

    public static void main(String[] args) {
        final EventBus bus = new EventBus();
        // QueryService内部实现 事件总线的注册
        QueryService queryService = new QueryService(bus);
        bus.register(new RequestQueryHandler(bus));
        queryService.query("NO-001");
    }

}

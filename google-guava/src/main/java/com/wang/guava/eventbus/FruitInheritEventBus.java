package com.wang.guava.eventbus;

import com.google.common.eventbus.EventBus;

/**
 * @description: Event继承关系 EventBus
 * @date: 2020/8/9 23:07
 * @author: wei·man cui
 */
public class FruitInheritEventBus {

    /**
     * 可以发现，子类事件传递后，事件父类和子类方法都被Listener接收执行了。
     * 但父类事件传递后，只有事件父类被Listener接收执行
     *
     * @param args
     */
    public static void main(String[] args) {
        final EventBus bus = new EventBus();
        bus.register(new FruitEaterListener());
        bus.post(new FruitApple("apple"));
        bus.post(new Fruit("Fruit"));
    }
}

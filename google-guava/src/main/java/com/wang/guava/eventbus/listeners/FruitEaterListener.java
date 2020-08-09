package com.wang.guava.eventbus.listeners;

import com.google.common.eventbus.Subscribe;
import com.wang.guava.eventbus.events.Fruit;
import com.wang.guava.eventbus.events.FruitApple;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: Event继承关系 消费者
 * @date: 2020/8/9 23:06
 * @author: wei·man cui
 */
@Slf4j
public class FruitEaterListener {

    @Subscribe
    public void eat(Fruit event) {
        if (log.isInfoEnabled()) {
            log.info("[Fruit] eat [{}].", event);
        }
    }

    @Subscribe
    public void eat(FruitApple event) {
        if (log.isInfoEnabled()) {
            log.info("[Apple] eat [{}].", event);
        }
    }


}

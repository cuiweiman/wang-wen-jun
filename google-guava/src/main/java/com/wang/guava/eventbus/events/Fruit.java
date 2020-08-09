package com.wang.guava.eventbus.events;

import com.google.common.base.MoreObjects;

/**
 * @description: Event继承关系
 * @date: 2020/8/9 16:34
 * @author: wei·man cui
 */
public class Fruit {

    private final String name;

    public Fruit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Name", name).toString();
    }
}

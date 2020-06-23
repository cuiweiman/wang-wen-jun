package com.wang.java8.stream;

import lombok.Data;

/**
 * @description: stream流 菜肴 实体类
 * @date: 2020/6/22 20:01
 * @author: weiman cui
 */
@Data
public class Dish {

    private final String name;

    private final boolean vegetarian;

    private final int calories;

    private final Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public enum Type {
        MEAT, FISH, OTHER
    }

}

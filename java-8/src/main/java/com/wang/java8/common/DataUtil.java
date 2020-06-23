package com.wang.java8.common;

import com.wang.java8.lambda.Apple;
import com.wang.java8.stream.Dish;

import java.util.Arrays;
import java.util.List;

/**
 * @description: demo中时用到的 数据
 * @date: 2020/6/23 20:04
 * @author: weiman cui
 */
public class DataUtil {
    public static List<Apple> appleList = Arrays.asList(
            new Apple("green", 150),
            new Apple("red", 127),
            new Apple("green", 115),
            new Apple("yellow", 170));

    public static List<Dish> menuList = Arrays.asList(
            new Dish("pork", false, 800, Dish.Type.MEAT),
            new Dish("beef", false, 700, Dish.Type.MEAT),
            new Dish("chicken", false, 400, Dish.Type.MEAT),
            new Dish("french fries", true, 530, Dish.Type.OTHER),
            new Dish("rice", true, 350, Dish.Type.OTHER),
            new Dish("season fruit", true, 120, Dish.Type.OTHER),
            new Dish("pizza", true, 550, Dish.Type.OTHER),
            new Dish("prawns", false, 300, Dish.Type.FISH),
            new Dish("salmon", false, 450, Dish.Type.FISH)
    );
}

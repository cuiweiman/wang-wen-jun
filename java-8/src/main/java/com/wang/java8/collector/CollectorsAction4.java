package com.wang.java8.collector;

import com.wang.java8.common.DataUtil;
import com.wang.java8.stream.Dish;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

/**
 * @description: Collectors
 * @date: 2020/6/28 21:27
 * @author: weiman cui
 */
public class CollectorsAction4 {
    public static void main(String[] args) {
        // testSummingDouble();
        // testToCollection();
        // testToConcurrentMap();
        // testToConcurrentMapWithBinaryOperator();
        // testToConcurrentMapWithBinaryOperatorAndSupplier();
        testToMap();
        testToSet();
        testToList();
    }


    /**
     * Collectors.summingDouble/summingInt/summingLong 统计总数
     */
    private static void testSummingDouble() {
        Optional.ofNullable(DataUtil.menuList.stream().collect(
                Collectors.summingDouble(Dish::getCalories)
        )).ifPresent(System.out::println);
        Optional.ofNullable(DataUtil.menuList.stream().map(Dish::getCalories)
                .mapToInt(Integer::intValue).sum()).ifPresent(System.out::println);
    }

    /**
     * Collectors.toCollection 转换成 集合
     */
    private static void testToCollection() {
        Optional.ofNullable(DataUtil.menuList.stream().filter(dish -> dish.getCalories() > 600).collect(
                Collectors.toCollection(LinkedList::new)))
                .ifPresent(System.out::println);
    }

    /**
     * Collectors.toConcurrentMap 生成 ConcurrentHashMap集合类
     */
    private static void testToConcurrentMap() {
        Optional.ofNullable(DataUtil.menuList.stream().filter(dish -> dish.getCalories() > 600).collect(
                Collectors.toConcurrentMap(Dish::getName, Dish::getCalories)))
                .ifPresent(System.out::println);
    }

    /**
     * 获得类型、数量
     */
    private static void testToConcurrentMapWithBinaryOperator() {
        Optional.ofNullable(DataUtil.menuList.stream().collect(
                Collectors.toConcurrentMap(Dish::getType, v -> 1L, (a, b) -> a + b)
        )).ifPresent(System.out::println);
    }

    /**
     * 返回类型是 跳表：获得类型、数量
     */
    private static void testToConcurrentMapWithBinaryOperatorAndSupplier() {
        Optional.ofNullable(DataUtil.menuList.stream().collect(
                Collectors.toConcurrentMap(Dish::getType,
                        v -> 1L,
                        (a, b) -> a + b,
                        ConcurrentSkipListMap::new)
        )).ifPresent(System.out::println);
    }

    /**
     * List 转换成 Map
     */
    private static void testToMap() {
        Optional.ofNullable(DataUtil.menuList.stream().collect(
                Collectors.toMap(Dish::getName, Dish::getCalories)
        )).ifPresent(System.out::println);
    }

    private static void testToSet() {
        Optional.ofNullable(DataUtil.menuList.stream().filter(Dish::isVegetarian).collect(
                Collectors.toSet())).ifPresent(System.out::println);
    }

    private static void testToList() {
        Optional.ofNullable(DataUtil.menuList.stream().filter(Dish::isVegetarian).collect(
                Collectors.toList())).ifPresent(System.out::println);
    }

}

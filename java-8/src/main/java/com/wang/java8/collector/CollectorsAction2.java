package com.wang.java8.collector;

import com.wang.java8.common.DataUtil;
import com.wang.java8.stream.Dish;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

/**
 * @description: Collectors
 * @date: 2020/6/26 9:38
 * @author: weiman cui
 */
public class CollectorsAction2 {
    public static void main(String[] args) {
        // testGroupingByConcurrentWithFunction();
        // testGroupingByConcurrentWithFunctionAndCollector();
        testGroupingByConcurrentWithFunctionAndSupplierAndCollector();
    }

    /**
     * ConcurrentHashMap
     */
    private static void testGroupingByConcurrentWithFunction() {
        ConcurrentMap<Dish.Type, List<Dish>> collect = DataUtil.menuList.stream().collect(Collectors.groupingByConcurrent(Dish::getType));
        Optional.ofNullable(collect.getClass()).ifPresent(System.out::println);
        Optional.ofNullable(collect).ifPresent(System.out::println);
    }

    /**
     * ConcurrentHashMap 平均数
     */
    private static void testGroupingByConcurrentWithFunctionAndCollector() {
        ConcurrentMap<Dish.Type, Double> collect = DataUtil.menuList.stream().collect(
                Collectors.groupingByConcurrent(Dish::getType, Collectors.averagingInt(Dish::getCalories)));
        Optional.ofNullable(collect.getClass()).ifPresent(System.out::println);
        Optional.ofNullable(collect).ifPresent(System.out::println);
    }

    /**
     * ConcurrentHashMap 跳表
     */
    private static void testGroupingByConcurrentWithFunctionAndSupplierAndCollector() {
        ConcurrentMap<Dish.Type, Double> collect = DataUtil.menuList.stream().collect(
                Collectors.groupingByConcurrent(Dish::getType,
                        ConcurrentSkipListMap::new,
                        Collectors.averagingInt(Dish::getCalories)));
        Optional.ofNullable(collect.getClass()).ifPresent(System.out::println);
        Optional.ofNullable(collect).ifPresent(System.out::println);
    }

}

package com.wang.java8.collector;

import com.wang.java8.common.DataUtil;
import com.wang.java8.stream.Dish;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: Collector API介绍
 * @date: 2020/6/23 20:03
 * @author: wei·man cui
 */
public class CollectorsAction {
    public static void main(String[] args) {
        // testAveragingDouble();
        // testCollectionAndThen();
        // testCounting();
        // testGroupingByFunction();
        // testGroupingByFunctionAndCollect();
        // testGroupingByFunctionAndCollect2();
        testGroupingByFunctionAndCollectAndSupplier();


    }

    // 求平均数 Collectors.averagingDouble/averagingInt/averagingLong
    private static void testAveragingDouble() {
        Optional.ofNullable(DataUtil.menuList.stream().collect(Collectors.averagingDouble(Dish::getCalories)))
                .ifPresent(System.out::print);
    }

    // Collectors.collectingAndThen  聚合 数据处理后，还进行其他操作
    private static void testCollectionAndThen() {
        Optional.ofNullable(DataUtil.menuList.stream().collect(Collectors.collectingAndThen(
                Collectors.averagingInt(Dish::getCalories),
                a -> "The result is " + a))).ifPresent(System.out::println);

        // 获取 不可修改的 集合: Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)
        // 获取不可修改的集合类： Collections.unmodifiableList(list);
        List<Dish> collect = DataUtil.menuList.stream().filter(d -> d.getType().equals(Dish.Type.MEAT))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        try {
            collect.remove(0);
        } catch (UnsupportedOperationException e) {
            System.out.println("不可修改");
        }
    }

    // 计数
    private static void testCounting() {
        Optional.ofNullable(DataUtil.menuList.stream().collect(Collectors.counting()))
                .ifPresent(System.out::println);
    }

    // 简单分组
    private static void testGroupingByFunction() {
        Optional.ofNullable(DataUtil.menuList.stream()
                .collect(Collectors.groupingBy(Dish::getType)))
                .ifPresent(System.out::println);
    }

    // 分组，计算各组元素数量
    private static void testGroupingByFunctionAndCollect() {
        Optional.ofNullable(DataUtil.menuList.stream().collect(Collectors
                .groupingBy(Dish::getType, Collectors.counting()))).ifPresent(System.out::println);
    }

    // 分组，计算各组 卡路里平均值
    private static void testGroupingByFunctionAndCollect2() {
        Optional.ofNullable(DataUtil.menuList.stream().collect(Collectors
                .groupingBy(Dish::getType, Collectors.averagingDouble(Dish::getCalories)))).ifPresent(System.out::println);
    }

    // 原结果是HashMap类型，转换成TreeMap类型 分组，计算各组 卡路里平均值。
    private static void testGroupingByFunctionAndCollectAndSupplier() {
        Map<Dish.Type, Double> map = DataUtil.menuList.stream().collect(Collectors.groupingBy(Dish::getType, Collectors.averagingDouble(Dish::getCalories)));
        System.out.println(map.getClass());
        Map<Dish.Type, Double> map2 = DataUtil.menuList.stream().collect(
                Collectors.groupingBy(Dish::getType, TreeMap::new, Collectors.averagingDouble(Dish::getCalories)));
        System.out.println(map2.getClass());
    }
}

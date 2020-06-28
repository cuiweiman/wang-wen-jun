package com.wang.java8.collector;

import com.wang.java8.common.DataUtil;
import com.wang.java8.stream.Dish;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.DoubleToIntFunction;
import java.util.stream.Collectors;

/**
 * @description: Collectors
 * @author: weiman cui
 * @date: 2020/6/28 18:09
 */
public class CollectorsAction3 {

    public static void main(String[] args) {
        // testPartitioningByWithPredicate();
        // testPartitioningByWithPredicateAndCollector();
        // testReducingBinaryOperator();
        // testReducingBinaryOperatorAndIdentity();
        // testReducingBinaryOperatorAndIdentityAndFunction();
        testSummarizingDouble();
    }

    /**
     * Collectors.partitioningBy：TRUE 和 FALSE 分组。是否是水果，进行分组
     */
    private static void testPartitioningByWithPredicate() {
        Map<Boolean, List<Dish>> collect = DataUtil.menuList.stream().collect(Collectors.partitioningBy(Dish::isVegetarian));
        System.out.println(collect);
    }

    /**
     * Collectors.partitioningBy：根据TRUE和FALSE 分组后，计算卡路里值
     */
    private static void testPartitioningByWithPredicateAndCollector() {
        Map<Boolean, Double> collect = DataUtil.menuList.stream().collect(
                Collectors.partitioningBy(Dish::isVegetarian,
                        Collectors.averagingInt(Dish::getCalories)));
        System.out.println(collect);
    }


    /**
     * Collectors.reducing 获取卡路里最大的Dish
     */
    private static void testReducingBinaryOperator() {
        DataUtil.menuList.stream().collect(
                Collectors.reducing(
                        BinaryOperator.maxBy(
                                Comparator.comparingInt(Dish::getCalories))))
                .ifPresent(System.out::println);
    }

    /**
     * Collectors.reducing 计算卡路里之和
     */
    private static void testReducingBinaryOperatorAndIdentity() {
        Integer collect = DataUtil.menuList.stream().map(Dish::getCalories)
                .collect(Collectors.reducing(0, (d1, d2) -> d1 + d2));
        System.out.println(collect);
    }

    /**
     * Collectors.reducing 计算卡路里之和
     */
    private static void testReducingBinaryOperatorAndIdentityAndFunction() {
        Integer collect = DataUtil.menuList.stream()
                .collect(Collectors.reducing(0, Dish::getCalories, (d1, d2) -> d1 + d2));
        System.out.println(collect);
    }

    /**
     * Collectors.summarizingDouble/summarizingLong/summarizingInt 统计
     */
    private static void testSummarizingDouble() {
        Optional.ofNullable(DataUtil.menuList.stream()
                .collect(Collectors.summarizingDouble(Dish::getCalories)))
                .ifPresent(System.out::println);
    }


}














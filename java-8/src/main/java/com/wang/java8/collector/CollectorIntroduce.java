package com.wang.java8.collector;

import com.wang.java8.common.DataUtil;
import com.wang.java8.lambda.Apple;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @date: 2020/6/23 19:53
 * @author: weiman cui
 */
public class CollectorIntroduce {
    public static void main(String[] args) {
        // Optional.ofNullable(getColor(DataUtil.list, "green")).ifPresent(System.out::println);
        groupByColor(DataUtil.appleList).forEach((k, v) -> System.out.println("[" + k + "]" + v));

    }

    /**
     * Collector 聚合
     *
     * @param list
     * @param color
     * @return
     */
    public static List<Apple> getColor(List<Apple> list, String color) {
        return list.stream().filter(a -> a.getColor().equals(color)).collect(Collectors.toList());
    }

    /**
     * Collector 分组
     *
     * @param list
     * @return
     */
    public static Map<String, List<Apple>> groupByColor(List<Apple> list) {
        return list.stream().collect(Collectors.groupingBy(Apple::getColor));
    }

}

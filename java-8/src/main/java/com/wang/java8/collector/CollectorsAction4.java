package com.wang.java8.collector;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wang.java8.common.DataUtil;
import com.wang.java8.stream.Dish;
import com.wang.java8.stream.PumpInfo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

/**
 * @description: Collectors
 * @date: 2020/6/28 21:27
 * @author: wei·man cui
 */
public class CollectorsAction4 {
    public static void main(String[] args) {
        // testSummingDouble();
        // testToCollection();
        // testToConcurrentMap();
        // testToConcurrentMapWithBinaryOperator();
        // testToConcurrentMapWithBinaryOperatorAndSupplier();
        // testToMap();
        // testToSet();
        // testToList();
        testGroupingBy();
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

    private static void testGroupingBy() {
        HashMap<String, List<PumpInfo>> map = DataUtil.pumpDataList.stream().collect(Collectors.groupingBy(pumpInfo ->
                new PumpInfo(pumpInfo.getHouseName(), pumpInfo.getGroupName(), pumpInfo.getPumpName())
                        .toString(), HashMap::new, Collectors.toList()));
        map.forEach((k, v) -> {
            System.out.print(k + " ==== ");
            System.out.println(v);
        });
        System.out.println("========================================");
        List<Map<String, Object>> result = Lists.newArrayList();
        // 泵房分组
        Map<String, List<PumpInfo>> pumpHouseMap =
                DataUtil.pumpDataList.stream().collect(Collectors.groupingBy(PumpInfo::getHouseName));
        pumpHouseMap.forEach((pumpHouseName, houseList) -> {
            Map<String, Object> pumpHouseData = Maps.newHashMap();
            pumpHouseData.put("houseName", pumpHouseName);
            List<Map<String, Object>> groupDataList = Lists.newArrayList();
            // 泵组分组
            Map<String, List<PumpInfo>> pumpGroupMap =
                    houseList.stream().collect(Collectors.groupingBy(PumpInfo::getGroupName));
            pumpGroupMap.forEach((pumpGroupName, groupList) -> {
                Map<String, Object> pumpGroupData = Maps.newHashMap();
                pumpGroupData.put("pumpGroupName", pumpGroupName);
                List<Map<String, Object>> pumpDataList = Lists.newArrayList();
                // 泵机分组
                Map<String, List<PumpInfo>> pumpMap =
                        groupList.stream().collect(Collectors.groupingBy(PumpInfo::getPumpName));
                pumpMap.forEach((pumpName, pumpList) -> {
                    List<String> indicatorList =
                            pumpList.stream().map(PumpInfo::getParam).collect(Collectors.toList());
                    Map<String, Object> pumpData = Maps.newHashMap();
                    pumpData.put("pumpName", pumpName);
                    pumpData.put("indicatorsNameList", indicatorList);
                    pumpDataList.add(pumpData);
                });
                pumpGroupData.put("pumpNameList", pumpDataList);
                groupDataList.add(pumpGroupData);
            });
            pumpHouseData.put("pumpGroup", groupDataList);
            result.add(pumpHouseData);
        });
        System.out.println(result.toString());
    }

}

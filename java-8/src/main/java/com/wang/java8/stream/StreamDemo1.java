package com.wang.java8.stream;

import com.wang.java8.common.DataUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: stream 案例
 * @date: 2020/6/22 20:02
 * @author: wei·man cui
 */
public class StreamDemo1 {

    private static List<String> getDishNamesByStream(List<Dish> menuList) {
        List<String> dishNameList = menuList.parallelStream().filter(dish -> {
            // 睡眠 100s 使用jconsole验证并行流的fork join
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return dish.getCalories() <= 400;
        })
                .sorted(Comparator.comparingInt(Dish::getCalories))
                .map(Dish::getName)
                .collect(Collectors.toList());
        return dishNameList;
    }

    public static void main(String[] args) {
        // 筛选卡路里400以下，且排序，最后获得菜肴名称
        List<String> result = getDishNamesByStream(DataUtil.menuList);
        System.out.println(result);
    }

}

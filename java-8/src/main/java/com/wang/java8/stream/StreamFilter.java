package com.wang.java8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: Stream Filter
 * @author: weiman cui
 * @date: 2020/6/23 13:08
 */
public class StreamFilter {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 7, 1);
        // filter——筛选获得偶数
        List<Integer> ouShu = list.stream().filter(n -> n % 2 == 0).collect(Collectors.toList());
        System.out.println(ouShu);

        // distinct——去重
        List<Integer> uniqueList = list.stream().distinct().collect(Collectors.toList());
        System.out.println(uniqueList);

        // skip——跳过前面3个元素
        List<Integer> result = list.stream().skip(3).collect(Collectors.toList());
        System.out.println(result);

        // limit——获取前3个元素
        List<Integer> collect = list.stream().limit(3).collect(Collectors.toList());
        System.out.println(collect);
    }

}

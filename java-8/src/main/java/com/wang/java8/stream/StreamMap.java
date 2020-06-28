package com.wang.java8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description:
 * @author: weiman cui
 * @date: 2020/6/23 13:13
 */
public class StreamMap {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 7, 1);

        List<Integer> collect = list.stream().map(n -> n * 2).collect(Collectors.toList());
        System.out.println(collect);

        // flatMap 扁平化
        String[] words = {"Hello World"};

        // {H,e,l,l,o} {W,o,r,l,d}
        Stream<String[]> stream = Arrays.stream(words).map(w -> w.split(""));
        // H e l l o  W o r l d
        Stream<String> stringStream = stream.flatMap(Arrays::stream);
        stringStream.distinct().forEach(System.out::println);

    }


}

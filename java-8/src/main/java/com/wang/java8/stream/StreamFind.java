package com.wang.java8.stream;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @description: find
 * @author: weiman cui
 * @date: 2020/6/23 13:39
 */
public class StreamFind {
    public static void main(String[] args) {
        Stream<Integer> stream = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7, 7, 1});
        Optional<Integer> any = stream.filter(i -> i % 2 == 0).findAny();
        System.out.println(any.get());

        stream = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7, 7, 1});
        Optional<Integer> any2 = stream.filter(i -> i > 10).findAny();
        System.out.println(any2.orElse(-1));

        stream = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7, 7, 1});
        Optional<Integer> first = stream.filter(i -> i % 2 == 0).findFirst();
        System.out.println(first.isPresent());

    }
}

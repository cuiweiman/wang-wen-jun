package com.wang.java8.stream;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @description: match
 * @author: weiman cui
 * @date: 2020/6/23 13:32
 */
public class StreamMatch {

    public static void main(String[] args) {
        Stream<Integer> stream = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7, 7, 1});
        // boolean b = stream.allMatch(i -> i % 2 == 0);
        // System.out.println(b);
        // boolean b1 = stream.anyMatch(i -> i > 0);
        // System.out.println(b1);
        boolean b2 = stream.noneMatch(i -> i == 1);
        System.out.println(b2);
    }

}

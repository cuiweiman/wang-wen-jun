package com.wang.java8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @description: Stream方法 自定义 构建
 * @date: 2020/6/22 20:55
 * @author: wei·man cui
 */
public class CreateStream2 {

    private static Stream<String> createStreamFromCollection() {
        List<String> list = Arrays.asList("Hello", "alex", "syy", "world", "cui");
        return list.stream();
        // 并行流中的数据 不是按照list顺序的
        // return list.parallelStream();
    }

    private static Stream<String> createStreamFromValues() {
        return Stream.of();
    }

    private static Stream<String> createStreamFromArrays() {
        return Arrays.stream(new String[]{"Hello", "alex", "syy", "world", "cui"});
    }

    public static void main(String[] args) {
        // createStreamFromCollection().forEach(System.out::println);
        // createStreamFromValues().forEach(System.out::println);
        createStreamFromArrays().forEach(System.out::println);
    }
}

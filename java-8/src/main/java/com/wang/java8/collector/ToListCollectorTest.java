package com.wang.java8.collector;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;

/**
 * @description: 测试自定义的Collector方法
 * @author: wei·man cui
 * @date: 2020/6/30 15:30
 */
public class ToListCollectorTest {

    public static void main(String[] args) {
        Collector<String, List<String>, List<String>> collector = new ToListCollector<>();
        String[] array = new String[]{"Alex", "syy", "Hello", "lambda", "collector", "java8", "stream"};
        /*List<String> result = Arrays.asList(array).parallelStream()
                .filter(ele -> ele.length() > 5)
                .collect(collector);*/
        List<String> result = Arrays.stream(array)
                .filter(ele -> ele.length() > 5)
                .collect(collector);
        System.out.println(result);

    }

}

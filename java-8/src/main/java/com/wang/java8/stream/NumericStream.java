package com.wang.java8.stream;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @description: Integer包装类相较于int，内存占用更多
 * @author: weiman cui
 * @date: 2020/6/23 17:07
 */
public class NumericStream {
    public static void main(String[] args) {
        Stream<Integer> stream = Arrays.stream(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        // Integer Stream 转 Int Stream
        int reduce = stream.mapToInt(i -> i.intValue()).filter(i -> i > 3).reduce(0, Integer::sum);
        System.out.println(reduce);

        // 100以内，获取以9为边的直角三角形
        int a = 9;
        IntStream.range(1, 100).filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                .boxed()
                .map(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)})
                .forEach(r -> System.out.println("a=" + r[0] + ", b=" + r[1] + ", c=" + r[2]));
        System.out.println();
        IntStream.rangeClosed(1, 100)
                .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)})
                .forEach(r -> System.out.println("a=" + r[0] + ", b=" + r[1] + ", c=" + r[2]));

    }


}

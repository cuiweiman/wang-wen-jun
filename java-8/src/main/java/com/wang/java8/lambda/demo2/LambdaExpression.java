package com.wang.java8.lambda.demo2;

import com.wang.java8.lambda.Apple;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @description: Lambda表达式语法
 * @date: 2020/6/21 20:12
 * @author: wei·man cui
 */
public class LambdaExpression {

    public static void main(String[] args) {
        /**
         * 根据苹果颜色进行 排序
         */
        Comparator<Apple> byColor = new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getColor().compareTo(o2.getColor());
            }
        };

        Comparator<Apple> byColor2 = (o1, o2) -> o1.getColor().compareTo(o2.getColor());

        Comparator<Apple> byColor3 = Comparator.comparing(Apple::getColor);

        List<Apple> list = Collections.emptyList();
        list.sort(byColor2);


        Function<String, Integer> fLambda = s -> s.length();

        Predicate<Apple> predicate = (Apple a) -> a.getColor().equals("green");


    }

}

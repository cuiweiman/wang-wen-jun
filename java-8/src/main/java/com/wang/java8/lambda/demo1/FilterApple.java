package com.wang.java8.lambda.demo1;

import com.wang.java8.common.DataUtil;
import com.wang.java8.lambda.Apple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description: Java Lambda 表达式初探
 * <p>
 * 根据需求，过滤苹果
 * </p>
 * @date: 2020/6/21 17:30
 * @author: weiman cui
 */
public class FilterApple {

    /**
     * 1. 根据苹果颜色进行苹果过滤
     * <p>
     * 如果现在要根据苹果大小，要怎么实现？java8之前：策略模式
     * </p>
     *
     * @param color
     * @param appleList
     * @return
     */
    public static List<Apple> findGreenApple(String color, List<Apple> appleList) {
        List<Apple> list = new ArrayList<>();
        for (Apple apple : appleList) {
            if (color.equals(apple.getColor())) {
                list.add(apple);
            }
        }
        return list;
    }


    /**
     * 2.1 策略模式 接口,根据不同的策略， 对苹果进行筛选
     * 根据需求，用不同的苹果筛选条件，实现本策略模式接口即可
     * <p>
     * 如果一个接口只有一个方法，那就可以理解是一个functional函数式
     * 使用注解：FunctionalInterface，就可以通过lambda表达式调用
     * </p>
     */
    @FunctionalInterface
    public interface AppleFilter {
        boolean filter(Apple apple);
    }

    /**
     * 2.2策略模式 实现类：筛选绿色、150重的苹果
     */
    public static class GreenAnd150Weight implements AppleFilter {
        @Override
        public boolean filter(Apple apple) {
            if ("green".equals(apple.getColor()) && 150L == apple.getWeight()) {
                return true;
            }
            return false;
        }
    }

    /**
     * 2.3根据策略模式，筛选苹果。满足策略则返回
     *
     * @param filter
     * @param appleList
     * @return
     */
    public static List<Apple> findApple(AppleFilter filter, List<Apple> appleList) {
        List<Apple> list = new ArrayList<>();
        for (Apple apple : appleList) {
            if (filter.filter(apple)) {
                list.add(apple);
            }
        }
        return list;
    }

    public static void main(String[] args) throws InterruptedException {
        // 普通需求实现
        /*List<Apple> appleList = findGreenApple("green", DataUtil.appleList);
        System.out.println(appleList);*/

        // 策略模式
        /*List<Apple> appleList = findApple(new GreenAnd150Weight(), list);
        System.out.println(DataUtil.appleList);*/

        /**
         * 利用策略模式的接口，筛选出黄色的苹果
         */
        /*List<Apple> apples = findApple(new AppleFilter() {
            @Override
            public boolean filter(Apple apple) {
                if ("yellow".equals(apple.getColor())) {
                    return true;
                }
                return false;
            }
        }, list);
        System.out.println(apples);*/

        /**
         * 利用 策略模式 函数式编程，实现lambda
         */
        /*List<Apple> lambdaResult = findApple(apple -> apple.getColor().equals("green"), list);
        System.out.println(lambdaResult);*/


        /**
         * Runnable 线程的 lambda表达式实现
         */
        // new Thread(() -> System.out.println(Thread.currentThread().getName())).start();
        // new Thread(() -> System.out.println(Thread.currentThread().getName())).start();

    }
}

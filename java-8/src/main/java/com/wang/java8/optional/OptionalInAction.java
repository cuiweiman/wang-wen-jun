package com.wang.java8.optional;

import java.util.Optional;

/**
 * @description:
 * @date: 2020/6/23 19:32
 * @author: wei·man cui
 */
public class OptionalInAction {
    public static void main(String[] args) {
        //如果传参 new Person()，依然会空指针异常，没有处理到
        String name = getInsuranceNameByFlatMap(null);
        System.out.println(name);

    }

    private static String getInsuranceNameByFlatMap(Person person) {
        return Optional.ofNullable(person)
                .flatMap(Person::getCarOptional)
                .flatMap(Car::getInsuranceOptional)
                .map(Insurance::getName)
                .orElse("UN_KNOW");
    }

}

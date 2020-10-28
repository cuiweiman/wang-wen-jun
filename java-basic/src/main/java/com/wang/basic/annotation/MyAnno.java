package com.wang.basic.annotation;

/**
 * @description: 自定义注解
 * @author: wei·man cui
 * @date: 2020/10/28 15:29
 */
public @interface MyAnno {

    // name 属性
    String name() default "";

    // value 属性
    String value() default "";

}

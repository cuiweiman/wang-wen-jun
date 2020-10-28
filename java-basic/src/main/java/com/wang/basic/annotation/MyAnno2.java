package com.wang.basic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 自定义注解
 * @author: wei·man cui
 * @date: 2020/10/28 15:29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)

public @interface MyAnno2 {

    // name 属性
    String name() default "";

    // value 属性
    String value() default "";

}

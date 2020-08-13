package com.wang.guava.eventbusself;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 自定义 Subscribe 注解
 * @date: 2020/8/11 23:47
 * @author: wei·man cui
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MySubscribe {

    String topic() default "default-topic";

}

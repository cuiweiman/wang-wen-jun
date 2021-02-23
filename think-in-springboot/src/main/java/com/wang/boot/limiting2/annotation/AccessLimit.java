package com.wang.boot.limiting2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 限流防刷 注解，seconds 秒内可以访问 maxCount 次接口
 * @author: wei·man cui
 * @date: 2021/2/23 10:06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {

    /**
     * 资源名称，用于描述接口功能
     */
    String name() default "";

    /**
     * 资源 key
     */
    String key() default "";

    /**
     * key prefix
     */
    String prefix() default "";

    /**
     * 时间范围，单位秒
     */
    long period();

    /**
     * 限制访问次数
     */
    int count();

}

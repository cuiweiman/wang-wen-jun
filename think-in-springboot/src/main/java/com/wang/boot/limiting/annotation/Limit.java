package com.wang.boot.limiting.annotation;

import com.wang.boot.limiting.enums.GlobalLimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 自定义 限流 注解
 * @author: wei·man cui
 * @date: 2021/2/22 17:45
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

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
    int period();

    /**
     * 限制访问次数
     */
    int count();

    /**
     * 限制类型
     */
    GlobalLimitType limitType() default GlobalLimitType.CUSTOMER;

}

package com.wang.think.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * @description: 动态aop  使用示例
 * @date: 2021/1/10 23:19
 * @author: wei·man cui
 */
@Aspect
public class Advisor {

    @Pointcut("execution(* *.test(..))")
    public void test() {

    }

    @Before("test()")
    public void before() {
        System.out.println("before test");
    }

    @After("test()")
    public void after() {
        System.out.println("after test");
    }

    @Around("test()")
    public Object around(ProceedingJoinPoint point) {
        System.out.println("around before");
        Object o = null;
        try {
            o = point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("around after");
        return o;
    }

}

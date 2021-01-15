package com.wang.think.aop.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK代理 AOP 的简单实现。
 * <p>
 * 在目标对象 执行之前 和 执行之后 进行了增强。Spring AOP 的实现其实也是使用了 Proxy 和 InvocationHandler。
 * <p>
 * 1. 在 构造函数 中将代理对象传入
 * 2. invoke 方法，此方法中实现了 AOP 增强的所有逻辑
 * 3. getProxy 方法，此方法千篇一律，但是必不可少。
 *
 * @description: 创建自定义的 MyInvocationHandler，用于对接口提供的方法进行增强
 * @author: wei·man cui
 * @date: 2021/1/15 11:41
 */
public class MyInvocationHandler implements InvocationHandler {

    /**
     * 目标对象
     */
    private Object target;

    public MyInvocationHandler(Object target) {
        super();
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("--------------------  before --------------------");
        Object result = method.invoke(target, args);
        System.out.println("--------------------  after --------------------");
        return result;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), target.getClass().getInterfaces(), this);
    }

}

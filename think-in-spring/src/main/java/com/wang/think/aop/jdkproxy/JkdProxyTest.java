package com.wang.think.aop.jdkproxy;

/**
 * @description: JDK代理 AOP 的简单实现
 * @author: wei·man cui
 * @date: 2021/1/15 11:45
 */
public class JkdProxyTest {

    public static void main(String[] args) {
        // 实例化 目标对象
        UserService userService = new UserServiceImpl();

        // 实例化  InvocationHandler
        final MyInvocationHandler handler = new MyInvocationHandler(userService);

        // 根据 目标对象 生成代理对象
        final UserService proxy = (UserService) handler.getProxy();

        // 调用 代理对象的 方法
        proxy.add();

    }

}

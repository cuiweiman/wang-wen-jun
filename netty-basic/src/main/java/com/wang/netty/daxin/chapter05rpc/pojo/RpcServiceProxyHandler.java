package com.wang.netty.daxin.chapter05rpc.pojo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @description: {@link RpcService} 的代理类
 * @author: wei·man cui
 * @date: 2021/4/25 16:15
 */
public class RpcServiceProxyHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("RpcServiceProxyHandler#invoke, " + method.getName() + " " + Arrays.toString(args));
        return new User();
    }
}

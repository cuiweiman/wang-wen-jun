package com.wang.think.aop.cglibproxy;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 在 执行 System.out.println(demo) 语句时，首先调用了 toString() 方法，然后是 hashCode() 方法，
 * 生成的对象为 com.wang.think.aop.cglibproxy.EnhancerDemo$$EnhancerByCGLIB$$87aa4e3a@2b05039f 的实例。
 * 这个类是由 CGLIB 生成的。
 * 完成CGLIB代理的类 是委托给 {@link org.springframework.aop.framework.CglibAopProxy} 类去实现的。
 *
 * @description: CGLIB 的使用示例
 * @author: wei·man cui
 * @date: 2021/1/15 15:35
 */
public class EnhancerDemo {

    public static void main(String[] args) {
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(EnhancerDemo.class);
        enhancer.setCallback(new MethodInterceptorImpl());

        final EnhancerDemo demo = (EnhancerDemo) enhancer.create();
        demo.test();
        System.out.println(demo);

    }

    public void test() {
        System.out.println("EnhancerDemo test()");
    }

    private static class MethodInterceptorImpl implements MethodInterceptor {
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            System.out.println("Before invoke " + method);
            final Object result = proxy.invokeSuper(obj, args);
            System.out.println("After invoke " + method);
            return result;
        }
    }
}


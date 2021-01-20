package com.wang.think.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 针对 Spring 容器中，所有的 bean 生效。
 * {@link #postProcessBeforeInitialization(Object, String)}：bean 调用构造函数实例化完成、依赖注入完成，在调用自定义初始化函数之前，触发
 * {@link #postProcessAfterInitialization(Object, String)}：bean 调用构造函数实例化完成、依赖注入完成，在调用自定义初始化函数结束后，触发
 *
 * @description: bean 生命周期 的 后处理器
 * @author: wei·man cui
 * @date: 2020/12/30 17:38
 */
public class MyBeanPostProcessor implements BeanPostProcessor {


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Book) {
            System.out.println("BeanPostProcessor.postProcessBeforeInitialization 方法调用：在自定义初始化方法执行之前 执行");
        }
        return bean;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Book) {
            System.out.println("BeanPostProcessor.postProcessAfterInitialization 方法调用：在自定义初始化方法执行之后 执行");
        }
        return bean;
    }


}

package com.wang.think.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @description: bean 生命周期 的 后置函数
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

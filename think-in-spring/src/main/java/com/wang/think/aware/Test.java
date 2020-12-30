package com.wang.think.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @description: 定义 BeanFactoryAware 类型的 Bean
 * @author: wei·man cui
 * @date: 2020/12/30 13:35
 */
public class Test implements BeanFactoryAware {

    private BeanFactory beanFactory;

    /**
     * 声明 Bean 的时候，Spring 会自动注入 BeanFactory
     *
     * @param beanFactory beanFactory
     * @throws BeansException 异常
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void testAware() {
        Hello hello = (Hello) beanFactory.getBean("hello");
        hello.say();
    }
}

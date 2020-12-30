package com.wang.think.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @description: Bean 的生命周期
 * @author: wei·man cui
 * @date: 2020/12/30 17:17
 */
public class Book implements BeanNameAware, BeanFactoryAware, ApplicationContextAware,
        InitializingBean, DisposableBean {

    private String bookName;

    private BeanFactory beanFactory;

    public Book() {
        System.out.println("【Book】 book 构造函数初始化");
    }


    @Override
    public void setBeanName(String s) {
        System.out.println("【Book】BeanNameAware.setBeanName，传入的参数s 是 bean name =" + s);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        System.out.println("【Book】BeanFactoryAware.setBeanFactory 方法调用，传入 beanFactory");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("【Book】ApplicationContextAware.setApplicationContext 方法调用，传入 应用的上下文引用");
    }


    @PostConstruct
    public void springPostConstruct() {
        System.out.println("【Book】@PostConstruct注解实现的：自定义的 bean 初始化方法");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("【Book】InitializingBean.afterPropertiesSet 自定义的 bean 初始化方法");
    }

    public void myPostConstruct() {
        System.out.println("【Book】XML 中配置的： 自定义的 bean 初始化方法");
    }

    @PreDestroy
    public void springPreDestroy() {
        System.out.println("【Book】@PreDestroy注解实现的： 自定义的 bean 销毁方法");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("【Book】DisposableBean.destroy 自定义的 bean 销毁方法");
    }

    public void myPreDestroy() {
        System.out.println("【Book】XML 中配置的： 自定义的 bean 销毁方法");
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("【Book】———————— Objects.finalize();  inside finalize ——————");
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}

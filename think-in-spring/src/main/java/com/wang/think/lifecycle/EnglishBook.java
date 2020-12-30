package com.wang.think.lifecycle;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringValueResolver;

/**
 * @description: 子类
 * @author: wei·man cui
 * @date: 2020/12/30 17:42
 */
public class EnglishBook extends Book implements BeanClassLoaderAware, ApplicationEventPublisherAware,
        EmbeddedValueResolverAware, EnvironmentAware, MessageSourceAware, ResourceLoaderAware {

    private String bookSystem;

    public String getBookSystem() {
        return bookSystem;
    }

    public void setBookSystem(String bookSystem) {
        System.out.println("【EnglishBook】设置 EnglishBook#bookSystem 的属性值 ");
        this.bookSystem = bookSystem;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("【EnglishBook】BeanClassLoaderAware.setBeanClassLoader 被调用了");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        System.out.println("【EnglishBook】ApplicationEventPublisherAware.setApplicationEventPublisher 被调用了");
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        System.out.println("【EnglishBook】EmbeddedValueResolverAware.setEmbeddedValueResolver 被调用了");
    }

    @Override
    public void setEnvironment(Environment environment) {
        System.out.println("【EnglishBook】EnvironmentAware.setEnvironment 被调用了");
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        System.out.println("【EnglishBook】MessageSourceAware.setMessageSource 被调用了");
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        System.out.println("【EnglishBook】ResourceLoaderAware.setResourceLoader 被调用了");
    }
}

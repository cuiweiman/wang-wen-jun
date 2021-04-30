package com.wang.nettyboot.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @description: SpringBoot 上下文，咋就拿不到 EventBus.class 的实体类呢？？
 * @author: wei·man cui
 * @date: 2021/4/30 9:54
 */
@Deprecated
public class SpringContextUtil implements ApplicationContextAware {

    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static ApplicationContext context = null;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}

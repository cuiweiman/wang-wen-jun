package com.wang.nettyboot.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @description: SpringBoot 上下文
 * @author: wei·man cui
 * @date: 2021/4/30 9:54
 */
@Deprecated
public class SpringContextComponent implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static Object getBean(String beanName) {
        return getApplicationContext().getBean(beanName);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextComponent.applicationContext = applicationContext;
    }
}

package com.wang.boot.getallcontrollerurl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @description: ApplicationContextAware 获取 Controller 所有的接口 url
 * @author: wei·man cui
 * @date: 2021/4/27 10:55
 */
@Slf4j
@Component
@Order(value = 2)
public class ControllerApplicationContext implements ApplicationContextAware, CommandLineRunner {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) throws Exception {
        final Map<String, Object> restControllerBeans = applicationContext.getBeansWithAnnotation(RestController.class);
        restControllerBeans.forEach((k, v) -> {
            final Class<?> targetClass = AopUtils.getTargetClass(v);
            log.info("拿到 controller：{}，拿到 value：{}，拿到Class：{}", k, v, targetClass);
            final RequestMapping mapping = targetClass.getDeclaredAnnotation(RequestMapping.class);

            final List<Method> methods = Arrays.asList(targetClass.getMethods());
            log.info("public methods:{}", methods);

            final List<Method> declaredMethods = Arrays.asList(targetClass.getDeclaredMethods());
            for (Method method : declaredMethods) {

            }


        });
    }


}

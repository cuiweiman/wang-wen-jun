package com.wang.think.iocbasic.apidi;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @description: 通过 Java Configuration注解的配置，将bean注入Spring的bean工厂
 * @author: wei·man cui
 * @date: 2021/1/29 17:55
 */
public class ApiConfigBean {
    public static void main(String[] args) {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApiBeanConfig.class);
        final ApiService apiService = (ApiService) context.getBean("apiService");
        apiService.sayHi();
    }
}

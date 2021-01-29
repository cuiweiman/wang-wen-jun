package com.wang.think.iocbasic.apidi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration注解：相当于 Spring 的 applicationContext.xml；
 * Bean注解：相当于 applicationContext.xml 配置的 <bean>标签
 *
 * @description: 通过 Java Configuration注解的配置，将bean注入Spring的bean工厂
 * @author: wei·man cui
 * @date: 2021/1/29 17:55
 */
@Configuration
public class ApiBeanConfig {
    @Bean
    ApiService apiService() {
        return new ApiService();
    }
}

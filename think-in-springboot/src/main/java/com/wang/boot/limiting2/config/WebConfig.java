package com.wang.boot.limiting2.config;

import com.wang.boot.limiting2.interceptor.AccessLimitInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @description: 注册拦截器
 * @author: wei·man cui
 * @date: 2021/2/23 10:27
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private AccessLimitInterceptor accessLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/register");
    }
}

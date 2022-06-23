package com.wang.alipay.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Objects;

/**
 * @description: 跨域配置
 * @author: wei·man cui
 * @date: 2021/1/13 11:15
 */
@Slf4j
@Configuration
public class CrossConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        log.info("【WebMvcConfigurer#configurePathMatch】 路由请求规则");
        WebMvcConfigurer.super.configurePathMatch(configurer);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 注册自定义的Formatter和Convert,例如, 对于日期类型,枚举类型的转化.
        log.info("【WebMvcConfigurer#addFormatters】 格式化转换");
        WebMvcConfigurer.super.addFormatters(registry);
    }

    /**
     * 请求参数处理器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        log.info("【WebMvcConfigurer#addArgumentResolvers】 请求参数处理器");
    }


    /**
     * 添加视图控制器
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        WebMvcConfigurer.super.addViewControllers(registry);
        log.info("【WebMvcConfigurer#addViewControllers】 添加视图控制器");
    }

    /**
     * 静态资源路径映射器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        log.info("【WebMvcConfigurer#addResourceHandlers】 静态资源管理器");
        // 访问url ip:port/path/static 时进入 classpath:/static 静态目录
        String os = System.getProperty("os.name");
        String windowsFilePath = "";
        String linuxFilePath = Objects.requireNonNull(
                        Thread.currentThread().getContextClassLoader().getResource("static"))
                .getPath();
        if (os.toLowerCase().startsWith("win")) {
            //如果是Windows系统
            registry.addResourceHandler("/static/**").addResourceLocations("file:" + windowsFilePath);
        } else { //linux和mac系统
            registry.addResourceHandler("/static/**").addResourceLocations("file:" + linuxFilePath + "/");
        }
    }

    /**
     * 拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    /**
     * 跨域请求配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("【WebMvcConfigurer#addCorsMappings】 跨域");
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }
}

package com.wang.boot.starter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 通过注解，进行配置的声明。扫描执行路径下的业务的 bean，以便在 Spring 启动时将 bean 载入容器中。
 * @author: wei·man cui
 * @date: 2021/1/28 17:39
 */
@Configuration
@ComponentScan({"com.wang.boot.starter"})
public class HelloServiceAutoConfig {
}

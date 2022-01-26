package com.wang.zookeeper;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2022/1/26 10:25
 */
@SpringBootApplication
public class ZookeeperApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ZookeeperApplication.class)
                .web(WebApplicationType.SERVLET)
                .registerShutdownHook(true)
                .run(args);
        System.out.println("\n zookeeper-demo run success ^_^ \n");
    }
}

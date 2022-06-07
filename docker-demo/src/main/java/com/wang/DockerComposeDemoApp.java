package com.wang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description: docker-compose 部署 springboot 项目 示例
 * @author: cuiweiman
 * @date: 2022/6/7 19:13
 */
@SpringBootApplication
public class DockerComposeDemoApp {

    public static void main(String[] args) {
        SpringApplication.run(DockerComposeDemoApp.class, args);
    }
}

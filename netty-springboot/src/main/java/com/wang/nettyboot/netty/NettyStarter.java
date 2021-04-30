package com.wang.nettyboot.netty;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: SpringBoot 项目启动后启动 Netty 服务器
 * @author: wei·man cui
 * @date: 2021/4/30 10:15
 */
@Order(value = -2)
@Component
public class NettyStarter implements CommandLineRunner {

    @Resource
    private NettyServer nettyServer;

    @Override
    public void run(String... args) throws Exception {
        nettyServer.start();
    }
}

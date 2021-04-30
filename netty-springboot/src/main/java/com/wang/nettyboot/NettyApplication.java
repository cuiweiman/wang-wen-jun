package com.wang.nettyboot;

import com.wang.nettyboot.netty.NettyStarter;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

/**
 * 直接在 main 方法中 启动 netty 服务端口；
 * 或者通过 实现 CommandLineRunner 接口启动{@link NettyStarter}
 *
 * @description: SpringBoot Netty
 * @author: wei·man cui
 * @date: 2021/4/30 9:45
 */
@SpringBootApplication
public class NettyApplication {
    public static void main(String[] args) {
        final ApplicationContext applicationContext = new SpringApplicationBuilder(NettyApplication.class)
                // .web(WebApplicationType.SERVLET)
                // 以 非 Web 的方式 启动
                .web(WebApplicationType.NONE)
                .registerShutdownHook(true)
                .run(args);
        /*final NettyServer nettyServer = applicationContext.getBean(NettyServer.class);
        nettyServer.start();*/
    }
}

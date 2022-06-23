package com.wang.zookeeper.watch;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2022/3/1 23:39
 */
@Configuration
public class ZkConfig {

    @Bean
    public ZooKeeper zooKeeper() {
        try {
            String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
            return new ZooKeeper(connectString, 2000, new ZkWatch());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

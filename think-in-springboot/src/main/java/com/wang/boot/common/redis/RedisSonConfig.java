package com.wang.boot.common.redis;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: RedisSon
 * @author: wei·man cui
 * @date: 2021/3/11 17:09
 */
@Configuration
public class RedisSonConfig {

    @Value("${spring.redis.host}")
    private String address;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.port}")
    private String port;

    @Bean
    public Redisson redisson() {
        // 单机模式配置演示
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://".concat(address).concat(":").concat(port))
                .setPassword(password)
                .setDatabase(0);
        return (Redisson) Redisson.create(config);
    }

}

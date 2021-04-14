package com.wang.boot.common.redis;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: RedisSon
 * @author: wei·man cui
 * @date: 2021/3/11 17:09
 */
@Configuration
public class RedisSonConfig {

    @Bean
    public Redisson redisson() {
        // 单机模式配置演示
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379")
                // .setPassword("sosms_2019")
                .setPassword("cuiweiman")
                .setDatabase(0);
        return (Redisson) Redisson.create(config);
    }

}

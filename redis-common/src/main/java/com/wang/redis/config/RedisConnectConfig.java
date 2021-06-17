package com.wang.redis.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * @Description: Redisson 配置
 * @Author: cuiweiman
 * @Since: 2021/6/10 上午9:55
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConnectConfig {

    private String host;

    private String port;

    private String password;

    private Integer database;

    private int connectionPoolSize;

    private int idleConnectionTimeout;

    private int connectTimeout;

    private int timeout;

    private static final String REDIS_SCHEMA = "redis";

    @Bean
    public RedissonClient clientWithStringCodec() {
        return this.createRedissonClient(new StringCodec());
    }

    /**
     * 配置 RedissonClient 对象
     *
     * @return RedissonClient
     * @throws IOException 异常
     */
    private RedissonClient createRedissonClient(BaseCodec baseCodec) {
        // TODO 通用组件，应该包含多种 redis 服务：单体、sentinel、cluster
        String address = UriComponentsBuilder.newInstance()
                .scheme(REDIS_SCHEMA).host(host)
                .port(port).toUriString();

        Config config = new Config();
        config.useSingleServer().setAddress(address).setDatabase(database)
                .setPassword(password)
                // 连接池 最大容量
                .setConnectionPoolSize(connectionPoolSize)
                // 最小连接数 默认 24
                .setConnectionMinimumIdleSize(24)
                // 如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒
                .setIdleConnectionTimeout(idleConnectionTimeout)
                // 连接超时时间
                .setConnectTimeout(connectTimeout)
                // 服务器响应时间
                .setTimeout(timeout);
        config.setCodec(baseCodec);
        return Redisson.create(config);
    }


}

package com.wang.es.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

import java.time.Duration;

/**
 * @description: 配置 ES 客户端连接信息
 * @date: 2020/11/22 22:08
 * @author: wei·man cui
 */
@Configuration
public class EsConfig {

    @Value("${es.client.host-and-port}")
    private String[] hostAndPort;

    @Value("${es.client.connect-time-out}")
    private Long connectTimeOut;

    @Value("${es.client.socket-time-out}")
    private Long socketTimeOut;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        ClientConfiguration config = ClientConfiguration.builder()
                .connectedTo(hostAndPort)
                .withConnectTimeout(Duration.ofSeconds(connectTimeOut))
                .withSocketTimeout(Duration.ofSeconds(socketTimeOut))
                .build();
        return RestClients.create(config).rest();
    }

}

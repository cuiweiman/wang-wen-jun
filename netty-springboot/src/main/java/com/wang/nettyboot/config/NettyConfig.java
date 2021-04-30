package com.wang.nettyboot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description: netty 端口配置
 * @author: wei·man cui
 * @date: 2021/4/30 9:52
 */
@Data
@Configuration
public class NettyConfig {

    @Value("${netty.server.port}")
    public Integer serverPort;

}

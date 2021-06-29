package com.wang.tsdb.config;

import com.baidubce.BceClientConfiguration;
import com.baidubce.Protocol;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.http.RetryPolicy;
import com.baidubce.services.tsdb.TsdbClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TSDB 客户端配置
 * @Author: cuiweiman
 * @Since: 2021/6/28 下午4:01
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "tsdb")
public class TsdbConfig {

    private String accessKeyId;

    private String secretAccessKey;

    private String endpoint;

    /**
     * 连接超时时间，毫秒
     */
    private Integer connectionTimeoutInMillis;

    /**
     * 最大连接数
     */
    private Integer maxConnections;

    /**
     * Socket缓冲区大小
     */
    private Integer socketBufferSizeInBytes;

    /**
     * 通过打开的连接传输数据的超时时间（单位：毫秒）
     */
    private Integer socketTimeoutInMillis;

    @Bean
    public TsdbClient bceClientConfiguration() {
        BceClientConfiguration config = new BceClientConfiguration()
                // 使用HTTPS协议;不设置，默认使用HTTP协议
                .withProtocol(Protocol.HTTPS)
                // 连接建立超时
                .withConnectionTimeoutInMillis(connectionTimeoutInMillis)
                .withMaxConnections(maxConnections)
                // Socket缓冲区大小,单位：Bytes
                .withSocketBufferSizeInBytes(socketBufferSizeInBytes)
                .withSocketTimeoutInMillis(socketTimeoutInMillis)
                // 默认 重试策略，没其他策略了。
                .withRetryPolicy(RetryPolicy.DEFAULT_RETRY_POLICY)
                .withCredentials(new DefaultBceCredentials(accessKeyId, secretAccessKey))
                .withEndpoint(endpoint);
        return new TsdbClient(config);
    }

}

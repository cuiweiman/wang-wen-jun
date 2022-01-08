package com.wang.kafka.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @description: WeChatConfig
 * @author: cuiweiman
 * @date: 2021/11/8 16:10
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "template")
public class WeChatTemplateProperties {

    private List<WeChatTemplate> templates;
    /**
     * 0-文件获取；1-数据库获取；2-ES获取
     */
    private int templateResultType;
    /**
     * 文件获取时，文件路径
     */
    private String resultFilePath;

    @Data
    public static class WeChatTemplate {
        private String templateId;
        private String templateFilePath;
        private boolean active;
    }
}

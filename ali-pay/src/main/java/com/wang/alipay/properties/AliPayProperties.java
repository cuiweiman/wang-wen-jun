package com.wang.alipay.properties;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 阿里支付 配置元素
 * @author: wei·man cui
 * @date: 2020/11/26 10:27
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ali-pay")
public class AliPayProperties {

    private String appId;

    private String privateKey;

    private String publicKey;

    private String gatewayUrl;

    private String notifyUrl;

    private String returnUrl;

    private String charset = "UTF-8";

    private String format = "JSON";

    private String signType = "RSA2";

    /**
     * 最大查询次数
     */
    private static int maxQueryRetry = 5;

    /**
     * 查询间隔（毫秒）
     */
    private static long queryDuration = 5000;

    /**
     * 最大撤销次数
     */
    private static int maxCancelRetry = 3;

    /**
     * 撤销间隔（毫秒）
     */
    private static long cancelDuration = 3000;

    @Override
    public String toString() {
        return "\nConfigs{" + "支付宝网关: " + gatewayUrl + "\n" +
                ", appId: " + appId + "\n" +
                ", 商户RSA私钥: " + getKeyDescription(privateKey) + "\n" +
                ", 支付宝RSA公钥: " + getKeyDescription(publicKey) + "\n" +
                ", 签名类型: " + signType + "\n" +
                ", 查询重试次数: " + maxQueryRetry + "\n" +
                ", 查询间隔(毫秒): " + queryDuration + "\n" +
                ", 撤销尝试次数: " + maxCancelRetry + "\n" +
                ", 撤销重试间隔(毫秒): " + cancelDuration + "\n" +
                "}";
    }

    private String getKeyDescription(String key) {
        // 隐藏一些字符
        int showLength = 12;
        if (StringUtils.isNotEmpty(key) && key.length() > showLength) {
            return key.substring(0, showLength) + "******" +
                    key.substring(key.length() - showLength);
        }
        return null;
    }
}

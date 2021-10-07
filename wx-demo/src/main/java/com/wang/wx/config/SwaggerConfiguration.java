package com.wang.wx.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Desc swagger
 * @Author cui·weiman
 * @Since 2021/10/7 16:32
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${swagger.switch}")
    private boolean enableSwagger;

    @Bean
    public Docket wechatAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enableSwagger)
                .groupName("WeChat APIs")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wang.wx.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("WeChat Demo RESTful APIs")
                .description("# WeChat Demo RESTful APIs")
                .termsOfServiceUrl("http://3353l10408.vicp.fun/wechat")
                .contact(new Contact("author", "http://127.0.0.1:8060/wechat", "email@163.com"))
                .version("V1.0")
                .build();
    }

}

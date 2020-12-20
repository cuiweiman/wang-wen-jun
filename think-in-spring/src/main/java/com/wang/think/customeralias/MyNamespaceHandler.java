package com.wang.think.customeralias;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @description: 扩展 NamespaceHandlerSupport，将XML中配置的组件注册到 Spring 容器中
 * @date: 2020/12/20 23:40
 * @author: wei·man cui
 */
public class MyNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("user", new UserBeanDefinitionParser());
    }
}

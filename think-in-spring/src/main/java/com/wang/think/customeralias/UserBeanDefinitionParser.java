package com.wang.think.customeralias;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;


/**
 * @description: 实现 BeanDefinitionReader 接口，解析 XSD 文件中的定义和组件定义
 * @date: 2020/12/20 23:34
 * @author: wei·man cui
 */
public class UserBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    /**
     * Element 对应的类
     *
     * @param element 元素
     * @return 类型
     */
    @Override
    @SuppressWarnings("rawtypes")
    protected Class getBeanClass(Element element) {
        return User.class;
    }


    /**
     * 解析XML元素自定义标签属性，并放入 BeanDefinitionHolder中
     * @param element 元素
     * @param builder BeanDefinition信息存放的容器
     */
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String userName = element.getAttribute("userName");
        String email = element.getAttribute("email");
        if (StringUtils.hasText(userName)) {
            builder.addPropertyValue("userName", userName);
        }
        if (StringUtils.hasText(email)) {
            builder.addPropertyValue("email", email);
        }

    }

}

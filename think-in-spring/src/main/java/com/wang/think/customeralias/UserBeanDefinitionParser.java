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
    protected Class getBeanClass(Element element) {
        return User.class;
    }

    /**
     * 从 element 中提取 属性值，并设置到对应的元素中，解析完成后 将bean注册到 beanFactory 中
     *
     * @param element 元素
     * @param bean    bean
     */
    protected void doParser(Element element, BeanDefinitionBuilder bean) {
        String userName = element.getAttribute("userName");
        String email = element.getAttribute("email");
        if (StringUtils.hasText(userName)) {
            bean.addPropertyValue("userName", userName);
        }
        if (StringUtils.hasText(email)) {
            bean.addPropertyValue("email", email);
        }
    }

}

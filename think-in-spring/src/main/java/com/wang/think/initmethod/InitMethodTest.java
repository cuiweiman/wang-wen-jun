package com.wang.think.initmethod;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: 初始化方法 测试
 * @author: wei·man cui
 * @date: 2020/12/30 13:54
 */
public class InitMethodTest {

    /**
     * 两个自定义初始化方法的 执行顺序：
     * 先执行了 实现的 InitializingBean 接口的 afterPropertiesSet 初始化方法，
     * 后执行了 配置在 xml 中的 init-method 初始化方法。
     * color 属性的设置 最终是 afterPropertiesSet 方法中设置的。
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beanFactoryTest.xml");
        Car car = (Car) context.getBean("car");
        System.out.println(car.getColor());
    }
}

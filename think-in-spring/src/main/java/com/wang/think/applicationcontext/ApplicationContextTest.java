package com.wang.think.applicationcontext;

import com.wang.think.factorybean.Car;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * @description: ApplicationContext：BeanFactory容器的功能扩展
 * @author: wei·man cui
 * @date: 2020/12/31 10:32
 */
public class ApplicationContextTest {

    public static void main(String[] args) {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new ClassPathResource("test/factorybean/spring-car.xml"));
        Car hello = (Car) xmlBeanFactory.getBean("car");
        System.out.println(hello.getBrand());

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("test/factorybean/spring-car.xml");
        Car hello2 = (Car) applicationContext.getBean("car");
        System.out.println(hello2.getBrand());

    }

}

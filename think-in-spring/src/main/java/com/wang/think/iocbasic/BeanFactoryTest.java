package com.wang.think.iocbasic;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * @description: 容器基本使用
 * @date: 2020/11/30 22:50
 * @author: wei·man cui
 */
public class BeanFactoryTest {

    /**
     * spring-beans 中 XmlBeanFactory 已过时，
     * 现在多使用 spring-context中的 ClassPathXmlApplicationContext类。
     * 分析 Spring 帮助我们完成了什么工作：
     * <p>
     * 1.读取 beanFactoryTest.xml （ConfigReader读取和验证配置文件）
     * 2. 根据 beanFactoryTest.xml，找到对应的类的配置（ReflectionUtil根据配置文件中的配置信息，通过反射进行实例化）
     * 3. 调用实例化后的实例。（APP应用：控制ConfigReader的读取、ReflectionUtil反射实例化的逻辑串联）
     * </p>
     */
    @Test
    public void testSimpleLoad() {
        BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("beanFactoryTest.xml"));
        MyTestBean myTestBean = (MyTestBean) beanFactory.getBean("myTestBean");
        assertEquals("testStr", myTestBean.getTestStr());
    }

    /**
     * 测试加载资源文件，获取内容
     *
     * @throws IOException 异常
     */
    @Test
    public void testLoadResource() throws IOException {
        ClassPathResource pathResource = new ClassPathResource("beanFactoryTest.xml");
        System.out.println(pathResource.getFilename());
        System.out.println(pathResource.getPath());
        System.out.println(pathResource.getURL().toString());
        System.out.println(pathResource.getURI().toString());
        System.out.println(pathResource.getDescription());
        System.out.println(pathResource.exists());

        try (InputStream inputStream = pathResource.getInputStream();
             FileOutputStream file = new FileOutputStream("E:/beanFactoryTest.xml");) {
            byte[] b = new byte[1024];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                file.write(b, 0, length);
            }
        }
    }

}

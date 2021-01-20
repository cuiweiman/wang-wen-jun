package com.wang.think.lifecycle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: 生命周期测试类
 * @author: wei·man cui
 * @date: 2020/12/30 17:34
 */
public class LifeCycleTest {
    public static void main(String[] args) {
        /*ApplicationContext context = new ClassPathXmlApplicationContext("test/lifecycle/book.xml");
        Book book = (Book) context.getBean("book");
        System.out.println("Book name = " + book.getBookName());
        ((ClassPathXmlApplicationContext) context).destroy();*/

        ApplicationContext context2 = new ClassPathXmlApplicationContext("test/lifecycle/english-book.xml");
        EnglishBook englishBook = (EnglishBook) context2.getBean("englishBook");
        System.out.println("englishBook system = " + englishBook.getBookSystem());
        ((ClassPathXmlApplicationContext) context2).destroy();
    }

    /*
    【Book】 book 构造函数初始化
    构造函数被调用了
    【EnglishBook】设置 EnglishBook#bookSystem 的属性值
    【Book】BeanNameAware.setBeanName，传入的参数s 是 bean name =englishBook
    【EnglishBook】BeanClassLoaderAware.setBeanClassLoader 被调用了
    【Book】BeanFactoryAware.setBeanFactory 方法调用，传入 beanFactory
    【EnglishBook】EnvironmentAware.setEnvironment 被调用了
    【EnglishBook】EmbeddedValueResolverAware.setEmbeddedValueResolver 被调用了
    【EnglishBook】ResourceLoaderAware.setResourceLoader 被调用了
    【EnglishBook】ApplicationEventPublisherAware.setApplicationEventPublisher 被调用了
    【EnglishBook】MessageSourceAware.setMessageSource 被调用了
    【Book】ApplicationContextAware.setApplicationContext 方法调用，传入 应用的上下文引用
    BeanPostProcessor.postProcessBeforeInitialization 方法调用：在自定义初始化方法执行之前 执行
    【Book】InitializingBean.afterPropertiesSet 自定义的 bean 初始化方法
    【Book】XML 中配置的： 自定义的 bean 初始化方法
    BeanPostProcessor.postProcessAfterInitialization 方法调用：在自定义初始化方法执行之后 执行
    englishBook system = Spring 源码深度解析
    17:51:28.894 [main] DEBUG org.springframework.context.support.ClassPathXmlApplicationContext - Closing org.springframework.context.support.ClassPathXmlApplicationContext@136432db, started on Wed Jan 20 17:51:28 CST 2021
    【Book】DisposableBean.destroy 自定义的 bean 销毁方法
    【Book】XML 中配置的： 自定义的 bean 销毁方法
     */
}

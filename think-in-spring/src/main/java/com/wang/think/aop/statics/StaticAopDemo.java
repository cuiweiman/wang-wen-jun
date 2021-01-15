package com.wang.think.aop.statics;

import com.wang.think.aop.TestBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 加载时织入（Load-Time Weaving，LTW）指的是在虚拟机载入字节码文件时动态织入AspectJ切面。
 * Spring框架的值添加为AspectJ LTW在动态织入的过程中提供了更细粒度的控制。
 * 使用Java（5+）的代理能使用一个叫“Vanilla”的AspectJ LTW，这需要在启动JVM的时候将某个JVM参数设置为开。
 * 这种JVM范围的设置在一些情况下或许不错，但通常情况下显得有些粗颗粒。
 * 而用Spring的LTW能让你在per-ClassLoader的基础上打开LTW，这显然更加细粒度并且对“单JVM多应用”的环境更具有意义
 * （例如在一个典型应用服务器环境中）。另外，在某些环境下，这能让你使用LTW而不对应用服务器的启动脚本做任何改动，
 * 不然则需要添加 -javaagent:path/to/aspectjweaver.jar 或者 -javaagent:path/to/Spring-agent.jar。
 * 开发人员只需要简单修改应用上下文的一个或几个文件就能使用LTW，而不需要依靠那些管理者部署配置，比如启动脚本的系统管理员。
 *
 * @description: 静态AOP使用示例
 * @author: wei·man cui
 * @date: 2021/1/15 16:37
 */
public class StaticAopDemo {

    public static void main(String[] args) {
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("test/aop/aop-static.xml");
        final TestBean test = (TestBean) applicationContext.getBean("test");
        test.test();
    }

}

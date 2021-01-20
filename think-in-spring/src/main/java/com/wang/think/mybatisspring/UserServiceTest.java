package com.wang.think.mybatisspring;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 通过本示例的配置文件来分析，不论是 MyBatis 的单独使用 还是 Spring整合MyBatis，最主要的都是 SqlSessionFactoryBean 类。
 * {@link SqlSessionFactoryBean}
 *
 * @description: Spring整合 MyBatis 测试
 * @author: wei·man cui
 * @date: 2021/1/20 8:55
 */
public class UserServiceTest {

    public static void main(String[] args) {
        final ApplicationContext context = new ClassPathXmlApplicationContext("test/mybatis-spring/spring.xml");
        final UserPoMapper userMapperPo = (UserPoMapper) context.getBean("userMapperPo");
        final UserPo userPo = userMapperPo.getUserPo(4);
        System.out.println(userPo.toString());

    }

}

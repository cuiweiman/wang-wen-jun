package com.wang.think.springjdbc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @description: Spring-Jdbc 测试
 * @author: wei·man cui
 * @date: 2021/1/15 17:30
 */
public class SpringJdbcDemo {

    public static void main(String[] args) {
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("test/springjdbc/spring-jdbc.xml");
        final UserService userService = (UserService) applicationContext.getBean("userService");

        User user = new User("刘亦菲", 30, "女");
        userService.save(user);

        final List<User> userList = userService.listUser();
        userList.forEach(System.out::println);
    }

}

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
}

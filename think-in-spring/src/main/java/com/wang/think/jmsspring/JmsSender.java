package com.wang.think.jmsspring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;

/**
 * @description: Spring-ActiveMQ 发送端
 * @author: wei·man cui
 * @date: 2021/1/28 13:55
 */
public class JmsSender {
    public static void main(String[] args) {
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/test/jmsspring/jms-spring.xml");
        final JmsTemplate jmsTemplate = (JmsTemplate) applicationContext.getBean("jmsTemplate");
        final Destination destination = (Destination) applicationContext.getBean("destination");
        jmsTemplate.send(destination, session -> session.createTextMessage("大家好，这是一个测试"));
    }
}

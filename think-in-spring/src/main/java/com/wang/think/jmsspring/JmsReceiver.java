package com.wang.think.jmsspring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;
import javax.jms.Message;

/**
 * @description: Spring-ActiveMQ 消费端；消费的另一种方式：消息监听器{@link JmsMsgListener}
 * @author: wei·man cui
 * @date: 2021/1/28 14:01
 */
public class JmsReceiver {
    public static void main(String[] args) {
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/test/jmsspring/jms-spring.xml");
        final JmsTemplate jmsTemplate = (JmsTemplate) applicationContext.getBean("jmsTemplate");
        final Destination destination = (Destination) applicationContext.getBean("destination");
        // 只能接收一次消息，如果未接收到消息，则会一直等待。可以设置 等待时间。可以通过创建 消息监听器来接收消息
        jmsTemplate.setReceiveTimeout(5000);
        final Message msg = jmsTemplate.receive(destination);
        System.out.println("接收到的消息：" + msg);
    }
}

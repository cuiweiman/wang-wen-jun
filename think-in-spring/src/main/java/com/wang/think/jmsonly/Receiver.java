package com.wang.think.jmsonly;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @description: JMS Java消息服务API的独立使用
 * @date: 2021/1/27 23:27
 * @author: wei·man cui
 */
public class Receiver {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("my-queue");
        MessageConsumer consumer = session.createConsumer(destination);
        int i = 0;
        while (i < 3) {
            i++;
            TextMessage message = (TextMessage) consumer.receive();
            session.commit();
            System.out.println("收到消息：" + message);
        }
        session.close();
        connection.close();
    }
}

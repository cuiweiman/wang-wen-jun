package com.wang.think.jmsonly;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @description: JMS Java消息服务API的独立使用
 * @date: 2021/1/27 23:27
 * @author: wei·man cui
 */
public class Sender {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("my-queue");
        MessageProducer producer = session.createProducer(destination);
        for (int i = 0; i < 3; i++) {
            TextMessage message = session.createTextMessage("Hello,this is a test");
            Thread.sleep(1000);
            producer.send(message);
        }
        session.commit();
        session.close();
        connection.close();
    }
}

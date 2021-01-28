package com.wang.think.jmsspring;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * <p>
 * 消息监听器，配置到 DefaultMessageListenerContainer 容器后，一旦服务端有消息传来，便会自动接收消息
 * </p>
 *
 * @description: 消息监听器：需要在配置文件中注册 消息容器，并将监听器注入到 容器中，才能使用
 * @author: wei·man cui
 * @date: 2021/1/28 14:11
 */
public class JmsMsgListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        TextMessage msg = (TextMessage) message;
        try {
            System.out.println(msg.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

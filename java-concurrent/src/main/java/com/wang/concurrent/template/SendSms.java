package com.wang.concurrent.template;

/**
 * 根据基类模板，构造发送短信的类
 * 只需要 实现 基类 中的 抽象方法 即可
 */
public class SendSms extends SendCustomer {
    @Override
    public void to() {
        System.out.println("To Jack");
    }

    @Override
    public void from() {
        System.out.println("From Rose");
    }

    @Override
    public void content() {
        System.out.println("Hello Jack!");
    }

    @Override
    public void send() {
        System.out.println("Send SMS");
    }

    public static void main(String[] args) {
        SendCustomer send = new SendSms();
        send.sendMsg();
    }

}

package com.cui.behavior.strategy;

/**
 * @description: 测试类
 * @date: 2020/7/20 23:05
 * @author: wei·man cui
 */
public class Client {
    public static void main(String[] args) {
        MovieTicket mt = new MovieTicket();
        double originalPrice = 60.0;
        double currentPrice;

        mt.setPrice(originalPrice);
        System.out.println("原始价为：" + originalPrice);
        System.out.println("---------------------------------");

        Discount discount;
        //读取配置文件并反射生成具体折扣对象
        discount = new ChildrenDiscount();
        //注入折扣对象
        mt.setDiscount(discount);

        currentPrice = mt.getPrice();
        System.out.println("折后价为：" + currentPrice);
    }
}

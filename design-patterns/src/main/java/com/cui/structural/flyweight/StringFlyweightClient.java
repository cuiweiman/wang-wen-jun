package com.cui.structural.flyweight;

/**
 * <p>
 *
 * </p>
 *
 * @description: 字符串 享元模式示例
 * @date: 2020/7/14 22:15
 * @author: weiman cui
 */
public class StringFlyweightClient {
    public static void main(String[] args) {
        String a = "Flyweight";
        String b = "Flyweight";
        String c = new String("Flyweight");
        System.out.println(a == b);
        System.out.println(b == c);
    }


}

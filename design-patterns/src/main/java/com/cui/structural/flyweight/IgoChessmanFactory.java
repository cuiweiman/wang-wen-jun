package com.cui.structural.flyweight;

import java.util.Hashtable;

/**
 * @description: 围棋棋子工厂类：享元工厂类，使用单例模式进行设计
 * @date: 2020/7/14 21:38
 * @author: wei·man cui
 */
public class IgoChessmanFactory {

    private static IgoChessmanFactory instance = new IgoChessmanFactory();

    //使用Hashtable来存储享元对象，充当享元池
    private static Hashtable ht;

    private IgoChessmanFactory() {
        ht = new Hashtable();
        IgoChessman black, white;
        black = new BlackIgoChessman();
        ht.put("b", black);
        white = new WhiteIgoChessman();
        ht.put("w", white);
    }

    // 获取 单例对象实例（饿汉式）
    public static IgoChessmanFactory getInstance() {
        return instance;
    }

    // 通过key 获取存储在Hashtable中的享元对象
    public static IgoChessman getIgoChessman(String color) {
        return (IgoChessman) ht.get(color);
    }

}

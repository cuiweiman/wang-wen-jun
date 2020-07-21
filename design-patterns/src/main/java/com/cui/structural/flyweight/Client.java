package com.cui.structural.flyweight;

/**
 * @description: 客户端测试代码
 * @date: 2020/7/14 22:06
 * @author: wei·man cui
 */
public class Client {

    public static void main(String[] args) {
        IgoChessman black1, black2, black3, white1, white2;
        IgoChessmanFactory factory;

        factory = IgoChessmanFactory.getInstance();

        black1 = factory.getIgoChessman("b");
        black2 = factory.getIgoChessman("b");
        black3 = factory.getIgoChessman("b");
        System.out.println(black1 == black2 && black2 == black3 && black1 == black3);

        white1 = factory.getIgoChessman("w");
        white2 = factory.getIgoChessman("w");
        System.out.println(white1 == white2);

        black1.display();
        black2.display();
        white1.display();
        white2.display();
    }

}

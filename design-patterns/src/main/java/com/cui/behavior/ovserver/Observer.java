package com.cui.behavior.ovserver;

/**
 * @description: 抽象观察类
 * @date: 2020/7/18 21:35
 * @author: wei·man cui
 */
public interface Observer {

    String getName();

    void setName(String name);

    void help();

    void beAttacked(AllyControlCenter acc);

}

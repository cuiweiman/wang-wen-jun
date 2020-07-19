package com.cui.behavior.ovserver;

import java.util.ArrayList;

/**
 * @description: 战队控制中心类：目标类
 * @date: 2020/7/18 21:39
 * @author: wei·man cui
 */
public abstract class AllyControlCenter {

    String allyName;

    ArrayList<Observer> players = new ArrayList<>();

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }

    public void join(Observer observer) {
        System.out.println(observer.getName() + " 加入 " + this.allyName + " 战队");
        players.add(observer);
    }

    public void quit(Observer observer) {
        System.out.println(observer.getName() + " 退出 " + this.allyName + " 战队");
        players.remove(observer);
    }

    // 声明抽象通知方法
    public abstract void notifyObserver(String name);

}

package com.cui.behavior.ovserver;

/**
 * @description: 具体战队控制中心类：具体目标类
 * @date: 2020/7/18 21:44
 * @author: wei·man cui
 */
public class ConcreteAllyControlCenter extends AllyControlCenter {

    public ConcreteAllyControlCenter(String allyName) {
        System.out.println(allyName + "战队创建成功");
        System.out.println("------------------");
        this.allyName = allyName;
    }

    /**
     * 通知其他队友支援
     *
     * @param name 需要支援的队友名字
     */
    @Override
    public void notifyObserver(String name) {
        System.out.println(this.allyName + " 战队紧急通知，盟友 " + name + "被攻击");
        players.parallelStream().forEach(observer -> {
            if (!observer.getName().equals(name)) {
                observer.help();
            }
        });

    }
}

package com.cui.creator.builderpattern;

/**
 * @description: 角色 建造起 抽象建造者
 * @date: 2020/7/9 23:20
 * @author: weiman cui
 */
public abstract class ActorBuilder {

    protected Actor actor = new Actor();

    public abstract void buildType();

    public abstract void buildSex();

    public abstract void buildFace();

    public abstract void buildCostume();

    public abstract void buildHairstyle();

    //工厂方法，返回一个完整的游戏角色对象
    public Actor createActor() {
        return actor;
    }
}

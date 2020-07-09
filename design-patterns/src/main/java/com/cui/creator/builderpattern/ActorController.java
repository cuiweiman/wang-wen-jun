package com.cui.creator.builderpattern;

/**
 * @description: 游戏角色创建控制器：指挥者
 * @date: 2020/7/9 23:24
 * @author: weiman cui
 */
public class ActorController {

    public Actor construct(ActorBuilder ab) {
        Actor actor;
        ab.buildType();
        ab.buildSex();
        ab.buildFace();
        ab.buildCostume();
        ab.buildHairstyle();
        actor = ab.createActor();
        return actor;
    }

}

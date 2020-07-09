package com.cui.creator.builderpattern;

import com.alibaba.fastjson.JSONObject;

/**
 * @description: 客户端测试代码
 * @date: 2020/7/9 23:25
 * @author: weiman cui
 */
public class Client {
    public static void main(String[] args) {
        ActorController actorController = new ActorController();
        // Actor actor = actorController.construct(new AngelBuilder());
        Actor actor = actorController.construct(new HeroBuilder());
        System.out.println(JSONObject.toJSONString(actor));
    }


}

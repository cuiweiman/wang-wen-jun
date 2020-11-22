package com.wang.es.javaclient;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * @description: 使用Java客户端 管理ES
 * @date: 2020/11/22 10:48
 * @author: wei·man cui
 */
public class Demo1 {

    public static void main(String[] args) {

    }

    public static void createIndex() throws Exception {
        // 1. 创建Settings对象，实现信息配置
        Settings settings = Settings.builder()
                .put("cluster.name", "my-application")
                .build();

        // 2. 创建客户端Client对象

        // 3. 使用Client对象创建一个索引库

        //4. 关闭Client对象


    }


}

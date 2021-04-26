package com.wang.netty.daxin.chapter05rpc.rpcclient;

import com.wang.netty.daxin.chapter05rpc.pojo.RpcService;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @description: RPC 协议实现
 * @author: wei·man cui
 * @date: 2021/4/25 11:10
 */
@Slf4j
public class RpcClient {

    public static void main(String[] args) {
        // 创建 与服务端的连接
        RpcClientFactory factory = new RpcClientFactory("127.0.0.1", 9090);
        // 获取 生产者 远程 API，并进行一次 远程调用
        final RpcService rpcService = factory.getRpcService();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                log.info("[RpcClient] {}", rpcService.findUserById(UUID.randomUUID().toString()));
                log.info("[RpcClient] {}", rpcService.rpcLogin("admin", "123"));
            }).start();
        }

        log.info("[RpcClient] {}", rpcService.rpcLogin("admin", "123"));
    }

}

package com.wang.netty.daxin.chapter05rpc.rpcserver;

import com.wang.netty.daxin.chapter05rpc.pojo.RpcService;
import com.wang.netty.daxin.chapter05rpc.pojo.User;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: RPC服务端 生产者 接口实现类
 * @author: wei·man cui
 * @date: 2021/4/25 16:44
 */
@Slf4j
public class RpcServiceImpl implements RpcService {
    @Override
    public User rpcLogin(String name, String password) {
        log.info("[服务生产者]RpcServiceImpl#rpcLogin，入参{}，{}", name, password);
        return new User(true);
    }
}

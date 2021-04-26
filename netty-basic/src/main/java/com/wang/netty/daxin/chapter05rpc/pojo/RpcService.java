package com.wang.netty.daxin.chapter05rpc.pojo;

/**
 * @description: RPC 服务，生产者 procedure
 * @author: wei·man cui
 * @date: 2021/4/25 16:12
 */
public interface RpcService {

    User rpcLogin(String name, String password);

    User findUserById(String id);

}

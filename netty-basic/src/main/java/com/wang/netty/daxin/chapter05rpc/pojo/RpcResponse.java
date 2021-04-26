package com.wang.netty.daxin.chapter05rpc.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 远程调用 返回 信息
 * @author: wei·man cui
 * @date: 2021/4/26 11:16
 */
@Data
public class RpcResponse implements Serializable {

    private String respId;

    private Object result;

}

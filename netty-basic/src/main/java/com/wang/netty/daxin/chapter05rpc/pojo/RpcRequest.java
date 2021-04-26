package com.wang.netty.daxin.chapter05rpc.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 远程调用接口参数信息
 * @author: wei·man cui
 * @date: 2021/4/26 10:34
 */
@Data
public class RpcRequest implements Serializable {

    private String reqId;

    /**
     * 服务 类名
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 具体参数的 类型
     */
    private Class[] paramsClass;

    /**
     * 具体参数的 数据
     */
    private Object[] params;

}

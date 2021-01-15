package com.wang.think.aop.jdkproxy;

/**
 * 创建业务接口，业务对外提供的接口，包含着业务对外提供的功能
 *
 * @description: JDK代理使用示例
 * @author: wei·man cui
 * @date: 2021/1/15 11:39
 */
public interface UserService {

    /**
     * 目标方法
     */
    void add();
}

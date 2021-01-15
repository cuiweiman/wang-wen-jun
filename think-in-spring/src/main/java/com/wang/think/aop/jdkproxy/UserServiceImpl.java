package com.wang.think.aop.jdkproxy;

/**
 * @description: 业务接口的实现类
 * @author: wei·man cui
 * @date: 2021/1/15 11:40
 */
public class UserServiceImpl implements UserService {
    /**
     * @see UserService#add()
     */
    @Override
    public void add() {
        System.out.println("-------------------- add --------------------");
    }
}

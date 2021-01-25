package com.wang.think.rmi;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/1/25 15:57
 */
public class HelloRmiServiceImpl implements HelloRmiService {

    @Override
    public int getAdd(int a, int b) {
        return a + b;
    }

}

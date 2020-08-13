package com.wang.mockito.lesson04;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2020/8/13 14:49
 */
public class StubbingService {
    public int getI() {
        System.out.println("=========【getI】");
        return 10;
    }

    public String getS() {
        System.out.println("=========【getS】");
        throw new RuntimeException();
    }
}

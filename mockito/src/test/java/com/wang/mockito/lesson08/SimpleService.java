package com.wang.mockito.lesson08;

import java.io.Serializable;
import java.util.Collection;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2020/8/14 10:23
 */
public class SimpleService {

    public int method1(int i, String s, Collection<?> c, Serializable serializable) {
        throw new RuntimeException();
    }

    public void method2(int i, String s, Collection<?> c, Serializable serializable) {
        throw new RuntimeException();
    }

}

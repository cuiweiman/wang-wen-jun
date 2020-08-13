package com.wang.mockito.lesson03.deepmock;

/**
 * @description: DeepMock 深层Mock
 * @author: wei·man cui
 * @date: 2020/8/13 11:42
 */
public class Lesson03Service {

    public Lesson03 get() {
        throw new RuntimeException();
    }

}

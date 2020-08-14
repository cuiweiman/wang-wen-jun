package com.wang.mockito.lesson10;

import org.junit.Test;

import static org.hamcrest.Matchers.both;
import static org.junit.Assert.assertThat;

/**
 * @description: 自定义 Matcher 比较器
 * @author: wei·man cui
 * @date: 2020/8/14 14:31
 */
public class SimpleThat {

    @Test
    public void test() {
        assertThat(10, CompareNumber.gt(5));
        assertThat(1, CompareNumber.lt(3));
        assertThat(12, both(CompareNumber.gt(10)).and(CompareNumber.lt(13)));
    }

}

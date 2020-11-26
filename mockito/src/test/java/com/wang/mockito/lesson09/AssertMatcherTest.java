package com.wang.mockito.lesson09;

import org.junit.Test;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.either;


/**
 * @description: Hamcrest Assert Matcher：断言风格
 * @author: wei·man cui
 * @date: 2020/8/14 13:42
 */
public class AssertMatcherTest {

    @Test
    public void test() {
        int i = 10;
        assertThat(i, equalTo(10));
        assertThat(i, is(10));
        assertThat(i, not(20));

        double price = 23.45;
        assertThat(price, either(equalTo(23.45)).or(equalTo(23.54)));
    }

    @Test
    public void test2() {
        double price = 23.45;
        // assertThat(price, both(equalTo(23.45)).and(equalTo(23.54)));
        assertThat(price, both(equalTo(23.45)).and(not(equalTo(23.54))));
        assertThat(price, anyOf(is(23.45), is(15.65), not(10.01)));

        assertThat(Stream.of(1, 2, 3).anyMatch(i -> i > 2), equalTo(true));
        assertThat(Stream.of(1, 2, 3).allMatch(i -> i > 0), equalTo(true));
    }

    // java.lang.NoSuchMethodError: org.hamcrest.Matcher.describeMismatch
    @Test
    public void test3() {
        double price = 23.45;
        assertThat("NotMatch", price, equalTo(23.4));
    }

}

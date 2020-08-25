package com.wang.guava.collections;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.fail;

/**
 * @description: Guava Immutable Collections 不可变集合
 * @author: wei·man cui
 * @date: 2020/8/25 17:20
 */
public class ImmutableCollectionsTest {

    /**
     * 修改测试
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testOf() {
        ImmutableList<Integer> list = ImmutableList.of(1, 2, 3);
        assertThat(list, notNullValue());
        // 不支持修改，excepted 处理 异常
        list.add(4);
        fail();
    }

    /**
     * ImmutableList.copyOf(array);
     */
    @Test
    public void testCopy() {
        Integer[] array = {1, 2, 3, 4, 5};
        ImmutableList<Integer> immutableList = ImmutableList.copyOf(array);
        System.out.println(immutableList);
    }


    @Test
    public void testBuilder() {
        ImmutableList<Integer> immutableList = ImmutableList.<Integer>builder().add(1).add(2).add(3)
                .addAll(Arrays.asList(4, 5)).build();
        System.out.println(immutableList);
    }

    @Test
    public void testImmutableMap() {
        ImmutableMap<String, String> map = ImmutableMap.<String, String>builder().put("SpringCloud", "2.5").put("Dubbo", "1.8").build();
        System.out.println(map);
    }

}

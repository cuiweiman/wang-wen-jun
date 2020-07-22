package com.wang.guava.utilities;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @description: 断言
 * @date: 2020/7/22 22:04
 * @author: wei·man cui
 */
public class PreconditionsTest {
    List<String> list = null;
    List<String> list2 = ImmutableList.of();

    /**
     * 校验 空指针异常
     */
    @Test
    public void checkNotNull() {
        Preconditions.checkNotNull(list);
    }

    /**
     * 校验 空指针异常，并返回指定 信息
     */
    @Test
    public void checkNotNullWithMsg() {
        try {
            Preconditions.checkNotNull(list, "The list should not be null");
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 校验 空指针异常，并返回 可以格式化的 指定信息
     */
    @Test
    public void checkNotNullWithFormatMsg() {
        try {
            Preconditions.checkNotNull(list,
                    "The list should not be null and the size must be %s", 2);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testCheckArguments() {
        try {
            Preconditions.checkArgument(false, "错误");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertThat(e.getClass(), is(IllegalArgumentException.class));
        }
    }

    @Test
    public void testCheckState() {
        try {
            Preconditions.checkState(false, "错误");
            fail("should not process to here");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertThat(e.getClass(), is(IllegalStateException.class));
        }
    }

    @Test
    public void testCheckIndex() {
        try {
            Preconditions.checkElementIndex(5, list2.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertThat(e.getClass(), is(IndexOutOfBoundsException.class));
        }
    }

    /**
     * JDK1.8 中的判断工具类
     */
    @Test(expected = NullPointerException.class)
    public void testByObjects() {
        Objects.requireNonNull(list);
    }

    /**
     * 语法糖 判空
     */
    @Test
    public void testAssertWithMessage() {
        try {
            assert list != null : "The list should not be null.";
        } catch (Error e) {
            System.out.println(e.getMessage());
            assertThat(e.getClass(), is(AssertionError.class));
        }
    }

}

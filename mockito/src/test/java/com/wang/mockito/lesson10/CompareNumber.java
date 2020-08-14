package com.wang.mockito.lesson10;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

/**
 * @description: 自定义 Matcher 比较器
 * @author: wei·man cui
 * @date: 2020/8/14 14:32
 */
public class CompareNumber<T extends Number> extends BaseMatcher<T> {
    private final T value;
    private final Compare<T> COMPARE;

    public CompareNumber(T value, boolean greater) {
        this.value = value;
        this.COMPARE = new DefaultNumberCompare<>(greater);
    }

    /**
     * 比大
     *
     * @param value 被比较的值
     * @param <T>   泛型
     * @return 比较结果
     */
    @Factory
    public static <T extends Number> CompareNumber<T> gt(T value) {
        return new CompareNumber<>(value, true);
    }

    /**
     * 比小
     *
     * @param value 被比较的值
     * @param <T>   泛型
     * @return 比较结果
     */
    @Factory
    public static <T extends Number> CompareNumber<T> lt(T value) {
        return new CompareNumber<>(value, false);
    }

    @Override
    public boolean matches(Object actual) {
        return this.COMPARE.compare(value, (T) actual);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("compare two number failed.");
    }

    /**
     * 扩展 接口
     *
     * @param <T>
     */
    private interface Compare<T extends Number> {
        boolean compare(T expected, T actual);
    }

    /**
     * 扩展 实现类
     *
     * @param <T>
     */
    private static class DefaultNumberCompare<T extends Number> implements Compare<T> {
        private final boolean greate;

        private DefaultNumberCompare(boolean greate) {
            this.greate = greate;
        }

        @Override
        public boolean compare(T expected, T actual) {
            Class<?> clazz = actual.getClass();
            if (clazz == Integer.class) {
                return greate ? (Integer) actual > (Integer) expected : (Integer) actual < (Integer) expected;
            } else if (clazz == Short.class) {
                return greate ? (Short) actual > (Short) expected : (Short) actual < (Short) expected;
            } else if (clazz == Byte.class) {
                return greate ? (Byte) actual > (Byte) expected : (Byte) actual < (Byte) expected;
            } else if (clazz == Double.class) {
                return greate ? (Double) actual > (Double) expected : (Double) actual < (Double) expected;
            } else if (clazz == Float.class) {
                return greate ? (Float) actual > (Float) expected : (Float) actual < (Float) expected;
            } else if (clazz == Long.class) {
                return greate ? (Long) actual > (Long) expected : (Long) actual < (Long) expected;
            } else {
                throw new AssertionError("The number type " + clazz + " not support.");
            }
        }
    }
}

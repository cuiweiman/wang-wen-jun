package com.wang.guava.cache.guavacache;

import com.google.common.base.Optional;
import com.google.common.cache.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

/**
 * @description: Guava Cache: CacheBuilder
 * @author: wei·man cui
 * @date: 2020/8/24 11:10
 */
public class CacheLoaderTest3 {

    /**
     * CacheLoader 不允许 根据 Key 获取到 Null 值。
     */
    @Test
    public void testLoadNullValue() {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .build(CacheLoader
                        .from(key -> Objects.equals(key, "null") ? null : new Employee(key, key, key)));
        Employee test = cache.getUnchecked("Test");
        assertThat(test, notNullValue());
        assertThat(test.getName(), equalTo("Test"));

        try {
            assertThat(cache.getUnchecked("null"), nullValue());
            fail("should not process to here.");
        } catch (Exception e) {
            assertThat(e instanceof CacheLoader.InvalidCacheLoadException, equalTo(Boolean.TRUE));
        }
    }

    /**
     * Guava Optional 处理 从缓存中获取到的 null 值
     */
    @Test
    public void testLoadNullValueUseOptional() {
        LoadingCache<String, Optional<Employee>> cache = CacheBuilder.newBuilder().build(CacheLoader
                .from(key -> Objects.equals(key, "null") ? Optional.fromNullable(null) : Optional.fromNullable(new Employee(key, key, key))));
        assertThat(cache.getUnchecked("Test").get(), notNullValue());
        assertThat(cache.getUnchecked("null").orNull(), nullValue());
        Employee defaultValue = new Employee("Default", "Default", "Default");
        assertThat(defaultValue.getName(), equalTo("Default"));
    }

    /**
     * Guava Cache refreshAfterWrite
     */
    @Test
    public void testCacheRefresh() throws InterruptedException {
        LoadingCache<String, Long> cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(2, TimeUnit.SECONDS)
                .build(CacheLoader.from(key -> System.currentTimeMillis()));
        cache.getUnchecked("Test");

        Long result1 = cache.getUnchecked("Test");
        TimeUnit.SECONDS.sleep(3);
        Long result2 = cache.getUnchecked("Test");
        assertThat(result1 != result2, equalTo(Boolean.TRUE));
    }

    /**
     * 缓存 中 预存 ？？？ 就是提前进行缓存。
     */
    @Test
    public void testCachePreload() {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().build(CacheLoader.from(String::toUpperCase));
        Map<String, String> preData = new HashMap<String, String>() {
            {
                put("test", "TEST");
                put("hello", "HELLO");
            }
        };
        cache.putAll(preData);
        assertThat(cache.size(), equalTo(2L));
        assertThat(cache.getUnchecked("test"), equalTo("TEST"));
    }

    /**
     * 缓存 移除元素时执行 函数
     */
    @Test
    public void testCacheRemoveNotification() {
        // 缓存监听器，一旦移除元素，就执行 自定义的方法
        RemovalListener<String, String> listener = notification -> {
            if (notification.wasEvicted()) {
                // 移除元素的原因
                assertThat(notification.getCause(), is(RemovalCause.SIZE));
                // 移除元素的 KEY 值
                assertThat(notification.getKey(), equalTo("Test"));
            }
        };
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                // 设置 缓存中最大元素个数
                .maximumSize(3)
                // 添加 监听器，监听 移除元素 的动作
                .removalListener(listener)
                .build(CacheLoader.from(String::toUpperCase));
        cache.getUnchecked("Test");
        cache.getUnchecked("Jack");
        cache.getUnchecked("Rose");
        cache.getUnchecked("Harry");
    }


}
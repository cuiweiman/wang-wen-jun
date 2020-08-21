package com.wang.guava.cache.guavacache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @description: Guava CacheLoader 的基本使用
 * @author: wei·man cui
 * @date: 2020/8/21 16:41
 */
public class CacheLoaderTest {
    @Test
    public void testBasic() throws Exception {
        // 定义 LoadingCache
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder().maximumSize(10)
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .build(createCacheLoader());
        Employee employee = cache.get("Alex");
        assertThat(employee, notNullValue());
        // 休眠40s，缓存数据会在30s过期，因此又会 重新 获取。
        TimeUnit.SECONDS.sleep(40);
        // 只会打印一条输出。没有从接口中获取，而是从Cache缓存中取值了。
        employee = cache.get("Alex");
        assertThat(employee, notNullValue());
    }

    /**
     * 测试 LRU清除策略：缓存元素为3
     */
    @Test
    public void testEvictionBySize() {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder().maximumSize(3).build(createCacheLoader());
        cache.getUnchecked("Alex");
        cache.getUnchecked("Jack");
        cache.getUnchecked("Rose");

        assertThat(cache.size(), equalTo(3L));
        cache.getUnchecked("Susan");
        assertThat(cache.size(), equalTo(3L));
        assertThat(cache.getIfPresent("Alex"), nullValue());
        assertThat(cache.getIfPresent("Susan"), notNullValue());
    }

    /**
     * 测试 LRU清除策略：缓存元素为3
     */
    @Test
    public void testEvictinByWeight() {
        Weigher<String, Employee> weigher = (key, employee) ->
                employee.getName().length() + employee.getEmpId().length() + employee.getDept().length();
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .maximumWeight(45)
                .concurrencyLevel(1)
                .weigher(weigher)
                .build(createCacheLoader());
        cache.getUnchecked("Gavin");
        cache.getUnchecked("Kevin");
        cache.getUnchecked("Allen");
        assertThat(cache.size(), equalTo(3L));
        assertThat(cache.getUnchecked("Gavin"), notNullValue());

        cache.getUnchecked("Jason");
        assertThat(cache.size(), equalTo(3L));
        assertThat(cache.getIfPresent("Kevin"), nullValue());
    }

    private CacheLoader<String, Employee> createCacheLoader() {
        return new CacheLoader<String, Employee>() {
            @Override
            public Employee load(String key) {
                return findEmployeeByName(key);
            }
        };
    }

    private Employee findEmployeeByName(final String name) {
        System.out.println("The employee " + name + " is load from DB.");
        return new Employee(name, name, name);
    }
}

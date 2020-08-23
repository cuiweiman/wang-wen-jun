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
 * @description: Guava LoadingCache 的基本使用。逐出策略：容量回收，定时回收，引用回收。
 * @author: wei·man cui
 * @date: 2020/8/21 16:41
 */
public class CacheLoaderTest {

    /**
     * 使用 LoadingCache 缓存获取数据
     *
     * @throws Exception 异常
     */
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
     * 测试 LRU清除策略：容量回收。
     * maximumSize：容量大小为3，超出则 根据最近最少使用算法 逐出元素。
     * （一般在 将要到达 最大容量时即会被回收）
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
     * 测试 LRU清除策略
     * maximumWeight：设置最大总权重，不能和maximumSize一起使用；
     * concurrencyLevel：并发数为1，Segment=1，那么就不会将maximumWeight分为多个Segment。
     * weigher：设置权重函数；45/1=45。
     * （一般在 将要到达 最大权重时即会被回收）
     */
    @Test
    public void testEvictingByWeight() {
        Weigher<String, Employee> weigher = (key, employee) ->
                employee.getName().length() + employee.getEmpId().length() + employee.getDept().length();
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .maximumWeight(45)
                .concurrencyLevel(1)
                .weigher(weigher)
                .build(createCacheLoader());
        // 15
        cache.getUnchecked("Gavin");
        // 15
        cache.getUnchecked("Kevin");
        // 15，一个Segment，15*3=45 达到最大权重。
        cache.getUnchecked("Allen");
        assertThat(cache.size(), equalTo(3L));
        assertThat(cache.getUnchecked("Gavin"), notNullValue());

        // 18，超出最大权重。根据最近最少使用原则，需要逐出两个旧的最少使用的元素（若只逐出1个，cache已使用权重=45，不足以放入18）
        cache.getUnchecked("123456");
        assertThat(cache.size(), equalTo(2L));
        assertThat(cache.getIfPresent("Kevin"), nullValue());
        assertThat(cache.getIfPresent("Allen"), nullValue());
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

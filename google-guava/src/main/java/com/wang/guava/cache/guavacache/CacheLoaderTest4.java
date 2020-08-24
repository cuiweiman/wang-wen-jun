package com.wang.guava.cache.guavacache;

import com.google.common.cache.*;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @description: Guava Cache Records
 * @author: wei·man cui
 * @date: 2020/8/24 14:34
 */
public class CacheLoaderTest4 {

    /**
     * 统计 缓存命中率等数据。
     * 首次获取数据时，Cache中内容为空，因此命中率为0，错失率为 1；
     */
    @Test
    public void testRecordStat() {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .recordStats().build(CacheLoader.from(String::toUpperCase));
        assertThat(cache.getUnchecked("test"), equalTo("TEST"));
        CacheStats stats = cache.stats();
        assertThat(stats.hitCount(), equalTo(0L));
        assertThat(stats.missCount(), equalTo(1L));

        assertThat(cache.getUnchecked("test"), equalTo("TEST"));
        // CacheStats对象是不可变的，因此需要重新创建。
        stats = cache.stats();
        assertThat(stats.hitCount(), equalTo(1L));
        assertThat(stats.missCount(), equalTo(1L));
        System.out.println(stats.hitRate() + "," + stats.missRate());
    }

    /**
     * 通过字符串 配置 CacheBuilder
     * 因此可以将 配置信息 写到 yml或者其他文件中进行配置。
     */
    @Test
    public void testCacheSpec() {
        String spec = "maximumSize=5,recordStats";
        CacheBuilderSpec builderSpec = CacheBuilderSpec.parse(spec);
        LoadingCache<String, String> cache =
                CacheBuilder.from(builderSpec).build(CacheLoader.from(String::toUpperCase));

        assertThat(cache.getUnchecked("test"), equalTo("TEST"));
        CacheStats stats = cache.stats();
        assertThat(stats.hitCount(), equalTo(0L));
        assertThat(stats.missCount(), equalTo(1L));

        assertThat(cache.getUnchecked("test"), equalTo("TEST"));
        // CacheStats对象是不可变的，因此需要重新创建。
        stats = cache.stats();
        assertThat(stats.hitCount(), equalTo(1L));
        assertThat(stats.missCount(), equalTo(1L));
        System.out.println(stats.hitRate() + "," + stats.missRate());
    }

}

package com.wang.guava.cache.guavacache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * <h2>定时回收</h2>
 * <p>
 * expireAfterAccess：指定项在一定时间内没有读写，会移除该key。
 * expireAfterWrite：在指定项在一定时间内没有 创建/更新时，会移除该key。
 * refreshAfterWrite：在指定时间内没有被创建/覆盖，则指定时间过后，再次访问时，会去刷新该缓存，在新值没有到来之前，始终返回旧值。
 * </p>
 * <h2>引用回收</h2>
 * <p>
 * WeakReference 弱引用：JVM发生FullGC或者MinorGC时，都会被回收。（MinorGC-年轻代回收，FullGC：整个堆内存回收）
 * SoftReference 软引用：内存空间足够，垃圾回收器就不会回收它；如果内存空间不足了，就会回收这些对象的内存。
 * </p>
 *
 * @description: Cache 缓存元素逐出策略：定时回收
 * @date: 2020/8/23 16:58
 * @author: wei·man cui
 */
public class CacheLoaderTest2 {

    /**
     * 定时回收 ==> expireAfterAccess()：设置定时时间。类似Redis的TTL。
     * Access Time ==>Write/Update/Read，即一段时间内没有读写，那么会被回收
     *
     * @throws InterruptedException 异常
     */
    @Test
    public void testEvictingByAccessTime() throws InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .expireAfterAccess(2, TimeUnit.SECONDS)
                .build(createCacheLoader());
        assertThat(cache.getUnchecked("Alex"), notNullValue());
        assertThat(cache.size(), equalTo(1L));

        // 休眠1秒，元素还会存在。
        TimeUnit.SECONDS.sleep(1);
        assertThat(cache.getIfPresent("Alex"), notNullValue());
        // 在休眠2秒，元素就会被逐出
        TimeUnit.SECONDS.sleep(2);
        assertThat(cache.getIfPresent("Alex"), nullValue());
    }

    /**
     * expireAfterWrite：write/update，即一段时间内没有被 创建或更新，那么会被回收
     */
    @Test
    public void testEvictingByWriteTime() throws InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .build(createCacheLoader());
        assertThat(cache.getUnchecked("Test"), notNullValue());
        assertThat(cache.size(), equalTo(1L));

        // 休眠1秒，元素还会存在。
        TimeUnit.SECONDS.sleep(1);
        assertThat(cache.getIfPresent("Test"), notNullValue());
        // 在休眠2秒，元素就会被逐出
        TimeUnit.SECONDS.sleep(2);
        assertThat(cache.getIfPresent("Test"), nullValue());
    }

    /**
     * Weak Reference 弱引用 作为缓存的 Key和Value
     */
    @Test
    public void testWeakKey() throws InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .weakValues().weakKeys().build(createCacheLoader());
        assertThat(cache.getUnchecked("Test"), notNullValue());
        assertThat(cache.getUnchecked("Guava"), notNullValue());
        // 手动触发GC，同步调用的异步方法，即不会被立即执行
        System.gc();
        TimeUnit.MILLISECONDS.sleep(500);
        assertThat(cache.getIfPresent("Test"), nullValue());
        assertThat(cache.getIfPresent("Guava"), nullValue());
        System.out.println(cache.size());
    }

    private CacheLoader<String, Employee> createCacheLoader() {
        return CacheLoader.from(key -> new Employee(key, key, key));
    }

}

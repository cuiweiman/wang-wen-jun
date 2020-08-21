package com.wang.guava.cache.cacheinmemory;

import com.wang.guava.cache.LruCache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description: 软引用 + LRU  实现 内存缓存
 * @author: wei·man cui
 * @date: 2020/8/21 11:43
 */
public class SoftReferenceLruCache<K, V> implements LruCache<K, V> {

    /**
     * 内部类形式 继承LinkedHashMap，可以避免外部类继承LinkedHashMap时，暴露Map的方法
     * 如此只暴露接口定义的方法，更好的封装。
     */
    private static class InternalLruCache<K, V> extends LinkedHashMap<K, SoftReference<V>> {

        private final int limit;

        public InternalLruCache(int limit) {
            super(16, 0.75f, true);
            this.limit = limit;
        }

        /**
         * LRU的关键：当现元素大小 超过了 最大限制值时（返回TRUE），移除最近最少使用的 键值对。
         *
         * @param eldest 最近最少使用的元素
         * @return 返回true，则移除集合中 最近最少使用的键值对
         */
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, SoftReference<V>> eldest) {
            return this.size() > limit;
        }
    }

    private final int limit;

    private final InternalLruCache<K, V> cache;

    public SoftReferenceLruCache(int limit) {
        this.limit = limit;
        this.cache = new InternalLruCache<>(limit);
    }

    @Override
    public void put(K key, V value) {
        this.cache.put(key, new SoftReference<>(value));
    }

    @Override
    public V get(K key) {
        SoftReference<V> ref = this.cache.get(key);
        if (null == ref) {
            return null;
        }
        return ref.get();
    }

    @Override
    public void remove(K key) {
        this.cache.remove(key);
    }

    @Override
    public int size() {
        return this.cache.size();
    }

    @Override
    public void clear() {
        this.cache.clear();
    }

    @Override
    public int limit() {
        return this.limit;
    }

}

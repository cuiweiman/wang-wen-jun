package com.wang.guava.cache;

import com.google.common.base.Preconditions;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 利用LinkedHashMap.removeEldestEntry() 实现的 LRU缓存
 * This class is not a thread-safe class.
 * </p>
 *
 * @description: 缓存 实现类.
 * @date: 2020/8/19 23:18
 * @author: wei·man cui
 */
public class LinkedHashLruCache<K, V> implements LruCache<K, V> {

    /**
     * 内部类形式 继承LinkedHashMap，可以避免外部类继承LinkedHashMap时，暴露Map的方法
     * 如此只暴露接口定义的方法，更好的封装。
     */
    private static class InternalLruCache<K, V> extends LinkedHashMap<K, V> {

        final private int limit;

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
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > limit;
        }
    }

    /**
     * 缓存限制，最多存放 limit 个元素
     */
    private final int limit;

    /**
     * 缓存容器。实际上就是 LinkedHashMap 集合。
     */
    private final InternalLruCache<K, V> internalLruCache;

    public LinkedHashLruCache(int limit) {
        Preconditions.checkArgument(limit > 0, "The limit should bigger than zero.");
        this.limit = limit;
        this.internalLruCache = new InternalLruCache<>(limit);
    }

    @Override
    public void put(K key, V value) {
        this.internalLruCache.put(key, value);
    }

    @Override
    public V get(K key) {
        return this.internalLruCache.get(key);
    }

    @Override
    public void remove(K key) {
        this.internalLruCache.remove(key);
    }

    @Override
    public int size() {
        return this.internalLruCache.size();
    }

    @Override
    public void clear() {
        this.internalLruCache.clear();
    }

    @Override
    public int limit() {
        return this.limit;
    }

    @Override
    public String toString() {
        return internalLruCache.toString();
    }
}
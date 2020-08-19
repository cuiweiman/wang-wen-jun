package com.wang.guava.cache;

/**
 * @description: LRU最近最少使用算法 实现的Cache缓存 接口
 * @date: 2020/8/19 23:17
 * @author: wei·man cui
 */
public interface LruCache<K, V> {

    void put(K key, V value);

    V get(K key);

    void remove(K key);

    int size();

    void clear();

    int limit();
}

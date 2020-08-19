package com.wang.guava.cache;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @description: 通过 双向链表 + HashMap 实现LRU缓存。非线程安全
 * @date: 2020/8/19 23:36
 * @author: wei·man cui
 */
public class LinkedListLruCache<K, V> implements LruCache<K, V> {

    /**
     * 缓存最大容量
     */
    private final int limit;

    /**
     * 存放 LRU缓存的 KEY
     */
    private final LinkedList<K> keys = new LinkedList<>();

    /**
     * 存放 LRU缓存的 KEY-VALUE
     */
    private final HashMap<K, V> cache = new HashMap<>();

    public LinkedListLruCache(int limit) {
        this.limit = limit;
    }

    @Override
    public void put(K key, V value) {
        K newKey = Preconditions.checkNotNull(key);
        V newValue = Preconditions.checkNotNull(value);

        if (keys.size() >= limit) {
            // 移除 最老的元素
            K oldKey = keys.removeFirst();
            V remove = cache.remove(key);
        }
        keys.add(key);
        cache.put(key, value);
    }

    @Override
    public V get(K key) {
        boolean exist = keys.remove(key);
        if (!exist) {
            // 删除失败，说明不存在，返回NULL。
            return null;
        }
        // 将使用过的元素 放到队尾。
        keys.addLast(key);
        return cache.get(key);
    }

    @Override
    public void remove(K key) {
        boolean exist = keys.remove(key);
        if (exist) {
            cache.remove(key);
        }
    }

    @Override
    public int size() {
        return keys.size();
    }

    @Override
    public void clear() {
        this.keys.clear();
        this.cache.clear();
    }

    @Override
    public int limit() {
        return this.limit;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (K k : keys) {
            builder.append(k).append(" = ").append(cache.get(k)).append("; ");
        }
        return builder.toString();
    }
}

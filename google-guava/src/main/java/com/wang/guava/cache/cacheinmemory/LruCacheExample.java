package com.wang.guava.cache.cacheinmemory;

import java.util.concurrent.TimeUnit;

/**
 * @description: LRU + SoftReference 实现内存缓存。测试Demo
 * @author: wei·man cui
 * @date: 2020/8/21 11:33
 */
public class LruCacheExample {
    public static void main(String[] args) throws InterruptedException {
        final SoftReferenceLruCache<String, byte[]> cache = new SoftReferenceLruCache<>(100);
        for (int i = 0; i < 100; i++) {
            cache.put(String.valueOf(i), new byte[1024 * 1024 * 2]);
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.println("The " + i + " entity is cached.");
        }
    }
}

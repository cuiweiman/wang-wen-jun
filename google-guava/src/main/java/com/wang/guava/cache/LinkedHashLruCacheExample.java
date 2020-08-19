package com.wang.guava.cache;

/**
 * @description: 测试 LinkedHashLruCache 最近最少使用算法 实现的缓存
 * @date: 2020/8/19 23:30
 * @author: wei·man cui
 */
public class LinkedHashLruCacheExample {

    public static void main(String[] args) {
        // 缓存 最大容量为3.当元素大于3时，则将最近最少使用的元素移除。
        LruCache<String, String> cache = new LinkedHashLruCache<>(3);
        cache.put("1", "1");
        cache.put("2", "2");
        cache.put("3", "3");
        cache.put("4", "4");
        System.out.println(cache);
        System.out.println("使用了 key=2 的元素"+cache.get("2"));
        System.out.println(cache);
    }

}

package com.wang.guava.cache;

/**
 * @description: LinkedListLruCache 测试类
 * @date: 2020/8/19 23:48
 * @author: wei·man cui
 */
public class LinkedListLruCacheExample {
    public static void main(String[] args) {
        LinkedListLruCache<String, String> cache = new LinkedListLruCache(3);
        cache.put("9", "1");
        cache.put("8", "2");
        cache.put("7", "3");
        cache.put("6", "4");
        System.out.println(cache);
        System.out.println("使用了 key=8 的元素" + cache.get("8"));
        System.out.println(cache);
    }

}

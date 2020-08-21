package com.wang.guava.cache;

import java.util.concurrent.TimeUnit;

/**
 * @description: 测试 LinkedHashLruCache 最近最少使用算法 实现的缓存
 * @date: 2020/8/19 23:30
 * @author: wei·man cui
 */
public class LinkedHashLruCacheExample {

    // 设置JVM运行内存：-Xmx128M -Xms64M -XX:+PrintGCDetails
    public static void main(String[] args) throws InterruptedException {
        // ordinaly();
        memoryTest();
    }

    // 内存溢出测试
    public static void memoryTest() throws InterruptedException {
        final LinkedHashLruCache<String, byte[]> cache = new LinkedHashLruCache<>(100);
        for (int i = 0; i < 100; i++) {
            // 每次放入2M的对象，JVM最大内存128M，因此循环不到64次（128/2 * 0.75 = 48次）就会发生内存溢出。
            cache.put(String.valueOf(i), new byte[1024 * 1024 * 2]);
            TimeUnit.MILLISECONDS.sleep(600);
            System.out.println("The " + i + " entity is cached.");
        }
    }


    // 普通测试
    public static void ordinaly() {
        // 缓存 最大容量为3.当元素大于3时，则将最近最少使用的元素移除。
        LruCache<String, String> cache = new LinkedHashLruCache<>(3);
        cache.put("1", "1");
        cache.put("2", "2");
        cache.put("3", "3");
        cache.put("4", "4");
        System.out.println(cache);
        System.out.println("使用了 key=2 的元素" + cache.get("2"));
        System.out.println(cache);
    }


}

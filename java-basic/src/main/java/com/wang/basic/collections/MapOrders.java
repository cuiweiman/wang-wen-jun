package com.wang.basic.collections;

import java.util.*;

/**
 * @description: Map集合的顺序
 * @author: wei·man cui
 * @date: 2021/2/4 10:17
 */
public class MapOrders {

    public static void main(String[] args) {
        hashMap();
        splitLine();
        hashTable();
        splitLine();
        treeMap();
        splitLine();
        linkedHashMap();
    }

    /**
     * 无序，根据 Key 的 HashCode 做散列寻址
     */
    public static void hashMap() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("Q", "5");
        hashMap.put("Z", "2");
        hashMap.put("C", "8");
        hashMap.put("Y", "0");
        hashMap.forEach((k, v) -> System.out.println("key=" + k + ";value=" + v));
    }

    /**
     * 无序
     */
    public static void hashTable() {
        Map<String, String> hashTable = new Hashtable<>();
        hashTable.put("B", "5");
        hashTable.put("Q", "3");
        hashTable.put("Z", "2");
        hashTable.put("C", "8");
        hashTable.put("Y", "0");
        hashTable.put("M", "0");
        hashTable.forEach((k, v) -> System.out.println("key=" + k + ";value=" + v));
    }

    /**
     * 自然顺序
     */
    public static void treeMap() {
        Map<String, String> treeMap = new TreeMap<>();
        treeMap.put("B", "5");
        treeMap.put("Q", "3");
        treeMap.put("Z", "2");
        treeMap.put("C", "8");
        treeMap.put("Y", "0");
        treeMap.put("M", "0");
        treeMap.forEach((k, v) -> System.out.println("key=" + k + ";value=" + v));
    }


    /**
     * 插入顺序 排序
     */
    private static void linkedHashMap() {
        Map<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("B", "5");
        linkedHashMap.put("Q", "3");
        linkedHashMap.put("Z", "2");
        linkedHashMap.put("C", "8");
        linkedHashMap.put("Y", "0");
        linkedHashMap.put("M", "0");
        linkedHashMap.forEach((k, v) -> System.out.println("key=" + k + ";value=" + v));
    }

    private static void splitLine() {
        System.out.println("-----------------");
    }
}

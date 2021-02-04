package com.wang.basic.collections;

import java.util.*;

/**
 * @description: Set集合是否有序(List要么是数组结构, 要么是链表结构, 都是插入顺序)
 * @author: wei·man cui
 * @date: 2021/2/4 9:44
 */
public class SetOrders {

    public static void main(String[] args) {
        hashSet();
        System.out.println();
        treeSet();
        System.out.println();
        linkedHashSet();
        System.out.println();
        listTest();
    }

    /**
     * HashSet 底层是 HashMap 结构，Hash 算法的寻址方式，无序
     */
    public static void hashSet() {
        Set<String> hashSet = new HashSet<>();
        hashSet.add("Q");
        hashSet.add("A");
        hashSet.add("Y");
        hashSet.add("F");
        hashSet.add(null);
        hashSet.forEach(System.out::print);
    }

    /**
     * TreeSet 底层是 TreeMap 结构，有序的，自然顺序
     */
    public static void treeSet() {
        Set<String> treeSet = new TreeSet<>();
        treeSet.add("Q");
        treeSet.add("A");
        treeSet.add("Y");
        treeSet.add("E");
        // treeSet.add(null);
        treeSet.forEach(System.out::print);
    }

    /**
     * 元素顺序是：插入顺序
     */
    public static void linkedHashSet() {
        Set<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("Q");
        linkedHashSet.add("A");
        linkedHashSet.add("Y");
        linkedHashSet.add("E");
        linkedHashSet.add(null);
        linkedHashSet.forEach(System.out::print);
    }

    public static void listTest() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add(null);
        arrayList.forEach(System.out::print);
        System.out.println();
        List<String> linkedList = new LinkedList<>();
        linkedList.add(null);
        linkedList.forEach(System.out::print);
        System.out.println();
        List<String> vector = new Vector<>();
        vector.add(null);
        vector.forEach(System.out::print);

    }

}

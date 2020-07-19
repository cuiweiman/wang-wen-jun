package com.cui.behavior.iterator;

import java.util.*;

/**
 * @description: 迭代器模式
 * @author: weiman cui
 * @date: 2020/7/16
 */
public class IteratorDemo {

    public static void process(Collection c) {
        Iterator i = c.iterator();
        //通过迭代器遍历聚合对象
        while (i.hasNext()) {
            System.out.println(i.next().toString());
        }
    }

    public static void main(String args[]) {
        Collection persons;
        // persons = new ArrayList();
        persons = new HashSet();
        persons.add("张无忌");
        persons.add("小龙女");
        persons.add("令狐冲");
        persons.add("韦小宝");
        persons.add("袁紫衣");
        persons.add("小龙女");
        process(persons);
    }
}

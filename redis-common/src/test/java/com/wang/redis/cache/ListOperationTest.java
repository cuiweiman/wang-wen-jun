package com.wang.redis.cache;

import com.wang.redis.RedisCommonAppTest;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;


public class ListOperationTest extends RedisCommonAppTest {

    @Resource
    private ListOperation<Object> rList;

    @Test
    public void test() {
        String key = "article:id";
        String ele1 = "1001";
        String ele2 = "1002";
        String ele3 = "1003";
        String ele4 = "1004";
        String ele5 = "1005";
        String ele6 = "1006";
        String ele7 = "1007";
        String ele8 = "1008";
        String ele9 = "1009";
        boolean add = rList.add(key, ele1);
        System.out.println(add);

        rList.fastAdd(key, 1, ele2);
        int i = rList.addAfter(key, ele2, ele4);
        System.out.println("addAfter new list size " + i);
        int j = rList.addBefore(key, ele4, ele3);
        System.out.println("addBefore  new list size " + j);

        List<Object> list = new ArrayList<>();
        list.add(ele5);
        list.add(ele6);
        list.add(ele7);
        list.add(ele8);
        list.add(ele9);
        boolean b = rList.addAll(key, list);
        System.out.println("addAll: " + b);

        System.out.println(rList.get(key, 0));
        rList.get(key, 1, 3, 5, 7, 9).forEach(System.out::print);
        System.out.println();

        List<Object> range = rList.range(key, 3, 5);
        range.forEach(System.out::print);
        System.out.println();

        System.out.println(rList.remove(key, 5));

        rList.trim(key, 2, 6);


    }
}
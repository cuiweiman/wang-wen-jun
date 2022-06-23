package com.wang.redis.cache;

import com.wang.redis.RedisCommonAppTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

public class SetOperationTest extends RedisCommonAppTest {

    @Resource
    private SetOperation<String> setOperation;


    @Test
    public void testBasic() {
        setOperation.addAll(key1, set1);

        Assertions.assertTrue(setOperation.add(key1, ele3));
        Assertions.assertTrue(setOperation.addAll(key2, set2));
        setOperation.addAll(key3, set3);

        Assertions.assertFalse(setOperation.tryAdd(key1, ele1, ele3, ele5, ele7, ele9));

        System.out.print(" random 3 : ");
        setOperation.random(key1, 3).forEach(System.out::print);
        System.out.println();

        System.out.println("remove: " + setOperation.remove(key1, ele1));

        System.out.println("removeRandom: " + setOperation.removeRandom(key1));

        Assertions.assertTrue(setOperation.del(key1));

        System.out.print("readAll : ");
        setOperation.readAll(key1).forEach(System.out::print);
        System.out.println();


        /*
        key1 空
        key2 听音乐、宅、看书、影视动漫、书法画画
        key3 旅游、打篮球、影视动漫、书法画画、骑行
        union 写入到redis key1：听音乐、宅、看书、影视动漫、书法画画、旅游、打篮球、骑行
        readIntersection 读结果：影视动漫、 书法画画
        diff 写入到redis：宅、看书、听音乐
         */

        // key2 并 key3 ，存入 key1
        System.out.println("union: " + setOperation.union(key1, key2, key3));
        // key2 交 key3
        // System.out.println("readIntersection: " + setOperation.readIntersection(key2, key3));
        // key2 差 key3，存入 key1
        // System.out.println("diff: " + setOperation.diff(key1, key2, key3));

        // key1 并 key2 并 key3, 只做返回，不做存储
        // setOperation.add(key1, ele1);
        // System.out.println("readUnion: " + setOperation.readUnion(key1, key2, key3));
    }


    private String key1 = null;
    private String key2 = null;
    private String key3 = null;
    private String ele1 = null;
    private String ele2 = null;
    private String ele3 = null;
    private String ele4 = null;
    private String ele5 = null;
    private String ele6 = null;
    private String ele7 = null;
    private String ele8 = null;
    private String ele9 = null;

    private Set<String> set1 = new HashSet<>();
    private Set<String> set2 = new HashSet<>();
    private Set<String> set3 = new HashSet<>();

    @BeforeAll
    public void init() {
        key1 = "tags:hobby:1001";
        key2 = "tags:hobby:1002";
        key3 = "tags:hobby:1003";
        ele1 = "游泳";
        ele2 = "听音乐";
        ele3 = "旅游";
        ele4 = "宅";
        ele5 = "打篮球";
        ele6 = "看书";
        ele7 = "影视动漫";
        ele8 = "书法画画";
        ele9 = "骑行";

        set1.add(ele1);
        set1.add(ele2);
        set1.add(ele5);
        set1.add(ele7);
        set1.add(ele9);

        set2.add(ele2);
        set2.add(ele4);
        set2.add(ele6);
        set2.add(ele7);
        set2.add(ele8);

        set3.add(ele3);
        set3.add(ele5);
        set3.add(ele7);
        set3.add(ele8);
        set3.add(ele9);
        setOperation.del(key1);
        setOperation.del(key2);
        setOperation.del(key3);

    }

}
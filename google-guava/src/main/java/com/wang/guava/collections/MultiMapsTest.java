package com.wang.guava.collections;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @description: Guava ArrayListMultimap 与 LinkedListMultimap
 * @author: wei·man cui
 * @date: 2020/8/25 11:01
 */
public class MultiMapsTest {

    @Test
    public void test() {
        HashMap<String, String> map = Maps.newHashMap();
        map.put("1", "1");
        map.put("1", "2");
        assertThat(map.size(), equalTo(1));

        // 可重复存储 Key的 Map. LinkedListMultimap：重复 key 对应的 Value 使用 LinkedList 格式存储。
        LinkedListMultimap<String, String> multimap = LinkedListMultimap.create();
        multimap.put("1", "1");
        multimap.put("1", "2");
        assertThat(multimap.size(), equalTo(2));
        System.out.println(multimap);
    }

}

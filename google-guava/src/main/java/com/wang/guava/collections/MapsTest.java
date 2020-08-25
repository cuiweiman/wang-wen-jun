package com.wang.guava.collections;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: Guava Maps、Multimaps、BiMap
 * @author: wei·man cui
 * @date: 2020/8/25 10:25
 */
public class MapsTest {

    @Test
    public void testCreate() {
        ArrayList<String> valueList = Lists.newArrayList("1", "2", "3");
        ImmutableMap<String, String> map = Maps.uniqueIndex(valueList, k -> k + "_key");
        System.out.println("[ImmutableMap——不可变的Map] " + map);

        Map<String, String> map1 = Maps.asMap(Sets.newHashSet("1", "2", "3"), k -> k + "_value");
        System.out.println(map1);
    }


    @Test
    public void testTransform() {
        Map<String, String> map = Maps.asMap(Sets.newHashSet("1", "2", "3"), k -> k + "_value");
        Map<String, String> transform = Maps.transformValues(map, v -> v + "_transform");
        System.out.println(transform);
    }

    @Test
    public void testFilter() {
        Map<String, String> map = Maps.asMap(Sets.newHashSet("1", "2", "3"), k -> k + "_value");
        Map<String, String> filter = Maps.filterKeys(map, k -> Lists.newArrayList("2", "3").contains(k));
        System.out.println("Guava "+filter);
        // java 8: k->k.getKey()/k.getValue()根据 key或value过滤
        Map<String, String> filter2 = map.entrySet().stream()
                .filter(k -> Lists.newArrayList("2", "3").contains(k.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("Java8 "+filter2);
    }

}

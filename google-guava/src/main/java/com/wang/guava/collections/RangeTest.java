package com.wang.guava.collections;

import com.google.common.collect.BoundType;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import org.junit.Test;

import java.util.NavigableMap;
import java.util.TreeMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @description: Guava Range：区间类型
 * @author: wei·man cui
 * @date: 2020/8/25 11:33
 */
public class RangeTest {

    /**
     * 闭区间 [0,9]
     */
    @Test
    public void testClosedRange() {
        Range<Integer> closed = Range.closed(0, 9);
        System.out.println(closed);
        assertThat(closed.contains(5), equalTo(true));
        assertThat(closed.contains(0), equalTo(true));
        assertThat(closed.contains(9), equalTo(true));
    }

    /**
     * 开区间 (0,9)
     */
    @Test
    public void testOpenRange() {
        Range<Integer> open = Range.open(0, 9);
        System.out.println(open);
        assertThat(open.contains(5), equalTo(true));
        assertThat(open.contains(0), equalTo(false));
        assertThat(open.contains(9), equalTo(false));
    }

    /**
     * 左闭右开 区间 [0,9)
     */
    @Test
    public void testClosedOpenRange() {
        Range<Integer> closedOpen = Range.closedOpen(0, 9);
        System.out.println(closedOpen);
        assertThat(closedOpen.contains(5), equalTo(true));
        assertThat(closedOpen.contains(0), equalTo(true));
        assertThat(closedOpen.contains(9), equalTo(false));
    }

    /**
     * 左开右闭 区间 (0,9]
     */
    @Test
    public void testOpenClosedRange() {
        Range<Integer> openClosed = Range.openClosed(0, 9);
        System.out.println(openClosed);
        assertThat(openClosed.contains(5), equalTo(true));
        assertThat(openClosed.contains(0), equalTo(false));
        assertThat(openClosed.contains(9), equalTo(true));
    }

    /**
     * 区间：大于 10 和 小于10
     */
    @Test
    public void testGreaterThan() {
        Range<Integer> greaterThan = Range.greaterThan(10);
        System.out.println(greaterThan);
        assertThat(greaterThan.contains(5), equalTo(false));
        assertThat(greaterThan.contains(10), equalTo(false));
        assertThat(greaterThan.contains(11), equalTo(true));
        Range<Integer> lessThan = Range.lessThan(5);
        System.out.println(lessThan);
    }

    @Test
    public void testMapRange() {
        // TreeMap有序，默认根据 ASCII码表顺序
        TreeMap<String, Integer> treeMap = Maps.newTreeMap();
        treeMap.put("Scala", 1);
        treeMap.put("Guava", 2);
        treeMap.put("Java", 3);
        treeMap.put("Kafka", 4);
        System.out.println("初始TreeMap： " + treeMap);

        NavigableMap<String, Integer> subMap = Maps.subMap(treeMap, Range.open("Guava", "Scala"));
        System.out.println(subMap);
    }

    @Test
    public void testOtherMethod() {
        // [10,+∞)
        Range<Integer> atLeast = Range.atLeast(10);
        System.out.println("atLeast=" + atLeast);

        // [10,+∞)
        Range<Integer> downToClose = Range.downTo(10, BoundType.CLOSED);
        System.out.println("downToClose=" + downToClose);

        // (10,+∞)
        Range<Integer> downToOpen = Range.downTo(10, BoundType.OPEN);
        System.out.println("downToOpen=" + downToOpen);

        // (-∞,10]
        Range<Integer> atMost = Range.atMost(10);
        System.out.println("atMost=" + atMost);

        // (-∞,10]
        Range<Integer> upToClosed = Range.upTo(10, BoundType.CLOSED);
        System.out.println("upToClosed=" + upToClosed);

        // (-∞,10)
        Range<Integer> upToOpen = Range.upTo(10, BoundType.OPEN);
        System.out.println("upToOpen=" + upToOpen);

        Range<Comparable<?>> all = Range.all();
        System.out.println("all=" + all);
    }

}

package com.wang.guava.collections;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @description: Guava Sets
 * @author: wei·man cui
 * @date: 2020/8/25 10:01
 */
public class SetsTest {

    @Test
    public void testCreate() {
        HashSet<Integer> set = Sets.newHashSet(1, 2, 3);
        assertThat(set.size(), equalTo(3));

        ArrayList<Integer> list = Lists.newArrayList(5, 6, 7, 8, 9);
        HashSet<Integer> set1 = Sets.newHashSet(list);
        assertThat(set1.size(), equalTo(5));

        // 笛卡尔积
        Set<List<Integer>> set2 = Sets.cartesianProduct(set, set1);
        System.out.println(set2);
    }

    /**
     * 长度为n的，所有子集
     */
    @Test
    public void testCombinations() {
        HashSet<Integer> aSet = Sets.newHashSet(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
        Set<Set<Integer>> combinations = Sets.combinations(aSet, 2);
        System.out.println(combinations.size());
        combinations.forEach(System.out::println);
    }

    /**
     * 差集、交集、并集
     */
    @Test
    public void testDifference() {
        HashSet<Integer> aSet = Sets.newHashSet(1, 2, 3, 4);
        HashSet<Integer> bSet = Sets.newHashSet(1, 3, 6);
        // 差集
        Sets.SetView<Integer> difference = Sets.difference(aSet, bSet);
        System.out.println("差集：" + difference);
        // 交集
        Sets.SetView<Integer> intersection = Sets.intersection(aSet, bSet);
        System.out.println("交集：" + intersection);
        // 并集
        Sets.SetView<Integer> union = Sets.union(aSet, bSet);
        System.out.println("并集：" + union);
    }
}

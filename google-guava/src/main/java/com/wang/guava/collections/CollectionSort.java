package com.wang.guava.collections;

import com.google.common.collect.Ordering;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @description: Guava Collections Sort
 * @author: wei·man cui
 * @date: 2020/8/25 17:35
 */
public class CollectionSort {

    /**
     * JDK 排序方法，按照自然顺讯排序：无法对有 null 元素的集合排序
     */
    @Test
    public void testJdkSort() {
        List<Integer> list = Arrays.asList(1, 5, 6, 3, null, 7, 2, 8, 0);
        Collections.sort(list);
        System.out.println(list);
    }

    /**
     * 集合中存在 null值 的排序方法。
     */
    @Test
    public void testOrderNaturalByNullFirstOrNullLast() {
        List<Integer> ordered = Arrays.asList(1, 5, 6, 3, 7, 2, 8, 0);
        Collections.sort(ordered);
        System.out.println("是否按照自然顺序排序：" + Ordering.natural().isOrdered(ordered));

        List<Integer> list = Arrays.asList(1, 5, 6, 3, null, 7, 2, 8, 0);
        Collections.sort(list, Ordering.natural().nullsFirst());
        System.out.println(list);

        Collections.sort(list, Ordering.natural().nullsLast());
        System.out.println(list);
    }

}

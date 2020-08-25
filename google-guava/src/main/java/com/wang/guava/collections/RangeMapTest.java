package com.wang.guava.collections;

import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @description: Guava RangeMap：区间 对应 Object
 * @author: wei·man cui
 * @date: 2020/8/25 16:54
 */
public class RangeMapTest {

    @Test
    public void testRangeMap() {
        TreeRangeMap<Integer, String> gradeRange = TreeRangeMap.create();
        gradeRange.put(Range.closed(0, 59), "C");
        gradeRange.put(Range.closed(60, 79), "B");
        gradeRange.put(Range.closed(80, 100), "A");
        assertThat(gradeRange.get(82), equalTo("A"));
        System.out.println(gradeRange.get(59));
    }

}

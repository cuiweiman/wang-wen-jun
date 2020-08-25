package com.wang.guava.collections;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @description: Guava Lists
 * @author: wei·man cui
 * @date: 2020/8/25 9:36
 */
public class ListsTest {

    /**
     * 笛卡尔积
     */
    @Test
    public void testCartesianProduct() {
        List<List<String>> result = Lists.cartesianProduct(
                Lists.newArrayList("1", "2"),
                Lists.newArrayList("A", "B")
        );
        System.out.println(result);
    }

    /**
     * Lists.transform()
     */
    @Test
    public void testTransform() {
        List<String> result = Lists.newArrayList("Scala", "Java", "Python");
        Lists.transform(result, e -> e.toUpperCase()).forEach(System.out::println);
    }

    @Test
    public void testNewArrayListWithCapacity() {
        ArrayList<Object> result = Lists.newArrayListWithCapacity(10);
        result.add("x");
        result.add("y");
        result.add("z");
        System.out.println(result);
    }

    @Test
    public void testNewArrayListWithExpectedSize() {
        Lists.newArrayListWithExpectedSize(3);
        // 写时拷贝（多线程）
        Lists.newCopyOnWriteArrayList();
    }

    /**
     * List反转顺序
     */
    @Test
    public void testReverse() {
        ArrayList<String> list = Lists.newArrayList("1", "2", "3");
        assertThat(Joiner.on(",").join(list), equalTo("1,2,3"));
        List<String> reverse = Lists.reverse(list);
        assertThat(Joiner.on(",").join(reverse), equalTo("3,2,1"));
    }

    /**
     * Partition：分组；提高数据操作的效率。
     */
    @Test
    public void testPartition() {
        ArrayList<String> list = Lists.newArrayList("1", "2", "3","4");
        List<List<String>> result = Lists.partition(list, 3);
        System.out.println(result.get(0));
        System.out.println(result.get(1));
    }

}

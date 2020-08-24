package com.wang.guava.collections;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @description: FluentIterable 抽象类集合
 * @author: wei·man cui
 * @date: 2020/8/24 16:00
 */
public class FluentIterableTest {

    private FluentIterable<String> build() {
        ArrayList<String> list = Lists.newArrayList("Java", "Guava", "Scala", ".Net");
        return FluentIterable.from(list);
    }

    /**
     * filter 过滤
     */
    @Test
    public void testFilter() {
        FluentIterable<String> fit = build();
        assertThat(fit.size(), equalTo(4));
        FluentIterable<String> result = fit.filter(e -> e != null && e.length() > 4);
        assertThat(result.size(), equalTo(2));
    }

    /**
     * Append 添加
     */
    @Test
    public void testAppend() {
        FluentIterable<String> fit = build();
        assertThat(fit.size(), equalTo(4));
        FluentIterable<String> append = fit.append("APPEND");
        assertThat(append.size(), equalTo(5));
        assertThat(append.contains("APPEND"), equalTo(Boolean.TRUE));
    }

    /**
     * 查询是否满足条件：allMatch、anyMatch、firstMatch
     */
    @Test
    public void testMatch() {
        FluentIterable<String> fit = build();
        boolean result1 = fit.allMatch(e -> e != null && e.length() >= 4);
        assertThat(result1, equalTo(Boolean.TRUE));

        boolean result2 = fit.anyMatch(e -> e != null && e.length() == 5);
        assertThat(result2, equalTo(Boolean.TRUE));

        Optional<String> optional = fit.firstMatch(e -> e != null && e.length() == 5);
        assertThat(optional.isPresent(), equalTo(Boolean.TRUE));
        assertThat(optional.get(), equalTo("Guava"));
    }

    /**
     * 获取 首/尾元素
     */
    @Test
    public void testFirstAndLast() {
        FluentIterable<String> fit = build();
        Optional<String> first = fit.first();
        assertThat(first.get(), equalTo("Java"));
        Optional<String> last = fit.last();
        assertThat(last.get(), equalTo(".Net"));
    }

    /**
     * Limit
     */
    @Test
    public void testLimit() {
        FluentIterable<String> fit = build();
        FluentIterable<String> limit = fit.limit(3);
        System.out.println(limit);
    }

    /**
     * 元素拷贝
     */
    @Test
    public void testCopyIn() {
        FluentIterable<String> fit = build();
        ArrayList<String> list = Lists.newArrayList("C#");
        ArrayList<String> result = fit.copyInto(list);
        System.out.println(result);
    }

    @Test
    public void testCycle() {
        FluentIterable<String> fit = build();
        FluentIterable<String> cycle = fit.cycle().limit(20);
        cycle.forEach(System.out::println);
    }

    /**
     *
     */
    @Test
    public void testTransform() {
        FluentIterable<String> fit = build();
        FluentIterable<Integer> cycle = fit.transform(e -> e.length());
        cycle.forEach(System.out::println);
    }

    @Test
    public void testJoin() {
        FluentIterable<String> fit = build();
        String result = fit.join(Joiner.on(','));
        assertThat(result, equalTo("Java,Guava,Scala,.Net"));
    }


    /**
     * A服务 通过 互联网 获取 B服务，可以批量查询，也可以一个一个查询。
     * 现查询 两种类型的会员列表，第一种是类型1，第二种类型是2
     */
    @Test
    public void testTransformAndConcatInAction() {
        ArrayList<Integer> cTypes = Lists.newArrayList(1, 2);
        FluentIterable<Customer> customers = FluentIterable.from(cTypes).transformAndConcat(this::search);
        customers.forEach(System.out::println);
    }

    private List<Customer> search(int type) {
        if (type == 1) {
            return Lists.newArrayList(new Customer(type, "Jack"), new Customer(type, "Rose"));
        } else {
            return Lists.newArrayList(new Customer(type, "Harry"), new Customer(type, "Ginny"));
        }
    }

    class Customer {
        final int type;
        final String name;

        public Customer(int type, String name) {
            this.type = type;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Customer{" + "type=" + type + ", name='" + name + '\'' + '}';
        }
    }

}

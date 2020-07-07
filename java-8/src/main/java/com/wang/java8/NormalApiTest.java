package com.wang.java8;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.*;

/**
 * @description: Java8 常见函数式接口的 使用demo
 * @author: weiman cui
 * @date: 2020/7/7 10:17
 */
public class NormalApiTest {
    List<String> list;

    @Before
    public void before() {
        list = Lists.newArrayList("1", "2", "3");
    }

    @Test
    public void testPredicate() {
        // boolean test(T t);
        // 传入的字符串是否以 .sql 结尾
        Predicate<String> isEndWithSql = (s) -> s.endsWith(".sql");
        // 传入的字符串非 .sql 结尾
        Predicate<String> notEndWithSql = isEndWithSql.negate();
        // 判断集合是否为空
        Predicate<List<String>> isEmptyList = List::isEmpty;
        Predicate<Integer> predicate = x -> x > 10;

        System.out.println(isEndWithSql.test("test.sql"));
        System.out.println(notEndWithSql.test("test.sql"));
        System.out.println(isEmptyList.test(Arrays.asList("1", "2", "3")));
        System.out.println(predicate.test(10));
    }

    @Test
    public void testBiPredicate() {
        System.out.println(biPredicate("a", "b", (String s1, String s2) -> s1.equals(s2)));
    }

    public static <T, U> boolean biPredicate(T t, U u, BiPredicate<T, U> biPredicate) {
        return biPredicate.test(t, u);
    }

    @Test
    public void testFunction() {
        // R apply(T t);
        // 字符串转为 Integer
        Function<String, Integer> toInteger = s -> Integer.valueOf(s);
        System.out.println(toInteger.apply("222"));
        toInteger = Integer::valueOf;
        System.out.println(toInteger.apply("222"));

        Function<Integer, String[]> fun = String[]::new;
        String[] strArr = fun.apply(10);
        System.out.println(strArr);

        System.out.println(function1(Lists.newArrayList("a", "aa", "aaa"), (String s) -> s.length()).toString());
    }

    @Test
    public void testBiFunction() {
        BiFunction<Integer, Integer, Integer> biFunction = (v1, v2) -> v1 + v2;

        System.out.println(biFunction.apply(2, 3));
        System.out.println(compute3(2, 3, (v1, v2) -> v1 + v2));
        System.out.println(compute3(2, 3, (v1, v2) -> v1 - v2));
        System.out.println(compute3(2, 3, (v1, v2) -> v1 * v2));
    }

    public int compute3(int a, int b, BiFunction<Integer, Integer, Integer> biFunction) {
        return biFunction.apply(a, b);
    }

    /**
     * Supplier correspond to POJO get
     * Consumer correspond to POJO set
     */
    @Test
    public void testSupplier() {
        // T get();
        Supplier<StringBuilder> sbSupplier = StringBuilder::new;
        StringBuilder sb = sbSupplier.get();
        Supplier<Date> sup = Date::new;
        Date date1 = sup.get();
        Date date2 = sup.get();
        System.out.println(date1 == date2);
    }

    @Test
    public void testConsumer() {
        // void accept(T t);
        Consumer<Runnable> runnableConsumer = (run) -> new Thread(run).start();
        runnableConsumer.accept(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            System.out.println("测试一下了");
        });
        Consumer<String> con = System.out::println;
        con.accept("测试一下了");

        consumer1(Lists.newArrayList(new Person(), new Person()), (Person person) -> person.setNation("Han"));
    }

    @Test
    public void testBiConsumer() {
        Person p1 = new Person();
        p1.setAge(10);
        Person p2 = new Person();
        p2.setAge(10);
        biConsumer(p1, p2, (Person orange11, Person orange22) -> orange11.setAge(orange11.getAge() + orange22.getAge()));
    }

    public static <T, U> void biConsumer(T t, U u, BiConsumer<T, U> biConsumer) {
        biConsumer.accept(t, u);
    }

    public static <T, R> List<R> function1(List<T> ts, Function<T, R> function) {
        List<R> list = Lists.newArrayList();
        ts.forEach(s -> {
            list.add(function.apply(s));
        });
        return list;
    }

    public static void consumer1(List<Person> person, Consumer<Person> consumer) {
        person.forEach(p -> {
            consumer.accept(p);
        });
    }

}

/**
 * JsonInclude： 为null的属性，不参与序列化
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
class Person {
    private String name;
    private int age;
    private String nation;

    public void print() {
    }
}
package com.wang.guava.utilities;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description: Joiner 字符串连接
 * @date: 2020/7/2 22:24
 * @author: wei·man cui
 */
public class JoinerTest {
    private final List<String> stringList = Arrays.asList(
            "Google", "Guava", "Java", "Scala", "Kafka"
    );
    private final List<String> stringListWithNullValue = Arrays.asList(
            "Google", "Guava", "Java", "Scala", null
    );

    private final Map<String, String> stringMap = ImmutableMap.of("Hello", "Guava", "Java", "Scala");

    private final String targetFileName = "E:\\guava-joiner.txt";

    /**
     * Joiner.on("delimiter").join()：使用特定的连接器，将集合中的元素连接成字符串
     */
    @Test
    public void testJoinerOnJoin() {
        String result = Joiner.on("#").join(stringList);
        Assert.assertThat(result, CoreMatchers.equalTo("Google#Guava#Java#Scala#Kafka"));
    }

    /**
     * Joiner.on("delimiter").join()情景：集合中存在null值，空指针异常
     */
    @Test(expected = NullPointerException.class)
    public void testJoinerOnJoinWithNullValue() {
        String result = Joiner.on("#").join(stringListWithNullValue);
        System.out.println(result);
        Assert.assertThat(result, CoreMatchers.equalTo("Google#Guava#Java#Scala#Kafka"));
    }

    /**
     * Joiner.on("delimiter").skipNulls().join： 跳过null值
     */
    @Test
    public void testJoinerOnJoinWithNullValueButSkip() {
        String result = Joiner.on("#").skipNulls().join(stringListWithNullValue);
        System.out.println(result);
        Assert.assertThat(result, CoreMatchers.equalTo("Google#Guava#Java#Scala"));
    }

    /**
     * Joiner.on("delimiter").useForNull("Default").join：
     * 使用自定义字符串，代替 集合中的null元素
     */
    @Test
    public void testJoinerOnJoinWithNullValueButUseDefaultValue() {
        String result = Joiner.on("#").useForNull("Default").join(stringListWithNullValue);
        System.out.println(result);
        Assert.assertThat(result, CoreMatchers.equalTo("Google#Guava#Java#Scala#Default"));
    }

    /**
     * Joiner.on("delimiter").useForNull("Default").appendTo(sb, stringListWithNullValue)
     * 将拼接好的字符串，添加到StringBuilder中去
     */
    @Test
    public void testJoinOnAppendToStringBuilder() {
        final StringBuilder sb = new StringBuilder();
        StringBuilder resultBuilder = Joiner.on("#").useForNull("Default")
                .appendTo(sb, stringListWithNullValue);
        System.out.println(resultBuilder.toString());
        Assert.assertThat(resultBuilder, CoreMatchers.sameInstance(sb));
    }


    /**
     * appendTo 将拼接好的字符串 写入到文件中去
     */
    @Test
    public void testJoinOnAppendToWriteFile() {
        try (FileWriter writer = new FileWriter(new File(targetFileName))) {
            Joiner.on("#").useForNull("Default").appendTo(writer, stringListWithNullValue);
            // assertThat(Files.isFile().test(new File(targetFileName)), equalTo(true));
        } catch (IOException e) {
            Assert.fail("append to the writer occur fetal error.");
        }
    }

    /**
     * Java8 实现 使用指定的连接器，将集合拼接成一个字符串,跳过null值
     */
    @Test
    public void testJoiningByStreamSkipNullValues() {
        Optional.of(stringListWithNullValue.stream()
                .filter(item -> item != null && !item.isEmpty())
                .collect(Collectors.joining("#")))
                .ifPresent(System.out::println);
    }

    /**
     * Java8 实现 使用指定的连接器，将集合拼接成一个字符串
     */
    @Test
    public void testJoiningByStreamUseDefaultValue() {
        Optional.of(stringListWithNullValue.stream()
                .map(item -> {
                    if (item != null && !item.isEmpty()) {
                        return "DEFAULT";
                    }
                    return item;
                }).collect(Collectors.joining("#")))
                .ifPresent(System.out::println);
    }

    /**
     * Joiner.on('delimiter').withKeyValueSeparator("=").join(map)：
     * 将 Map 的 元素，以 Key=value的形式结合成字符串，
     * 并使用 delimiter 分隔符划分开来
     */
    @Test
    public void testJoinOnWithMap() {
        Optional.of(Joiner.on('#').withKeyValueSeparator("=").join(stringMap))
                .ifPresent(System.out::println);
    }

    /**
     * Joiner.on('delimiter').withKeyValueSeparator("=").join(map)：
     * 将 Map 的 元素，以 Key=value的形式结合成字符串，
     * 并使用 delimiter 分隔符划分开来；
     * 将字符串输出到 目标文件中
     */
    @Test
    public void testJoinOnWithMapToAppendable() {
        try (FileWriter writer = new FileWriter(new File(targetFileName))) {
            Joiner.on('#').withKeyValueSeparator("=").appendTo(writer, stringMap);
            System.out.println(Files.isFile().test(new File(targetFileName)));
        } catch (IOException e) {
            Assert.fail("append to the writer occur fetal error.");
        }
    }
}

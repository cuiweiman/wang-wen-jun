package com.wang.guava.utilities;

import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @description: 按照指定的分隔符，将字符串分隔成一个集合
 * @date: 2020/7/2 23:00
 * @author: wei·man cui
 */
public class SplitterTest {

    /**
     * 根据 分隔符 分隔字符串，返回 List
     */
    @Test
    public void testSplitOnSplit() {
        List<String> result = Splitter.on("|").splitToList("hello|world");
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(2));
        assertThat(result.get(0), equalTo("hello"));
        assertThat(result.get(1), equalTo("world"));
        result.forEach(System.out::println);
    }

    /**
     * 根据 分隔符 分隔字符串，忽略空值，返回 List
     */
    @Test
    public void testSplitOnSplitOmitEmpty() {
        List<String> result = Splitter.on("|").splitToList("hello|world|||");
        assertThat(result.size(), equalTo(5));
        result = Splitter.on("|").omitEmptyStrings().splitToList("hello|world|||");
        assertThat(result.size(), equalTo(2));
    }

    /**
     * 分隔结果 去除空格
     */
    @Test
    public void testSplitOnSplitOmitEmptyTrimResult() {
        List<String> result = Splitter.on("|").omitEmptyStrings()
                .splitToList(" hello|world |||");
        assertThat(result.size(), equalTo(2));
        assertThat(result.get(0), equalTo(" hello"));
        assertThat(result.get(1), equalTo("world "));
        result = Splitter.on("|").omitEmptyStrings().trimResults().splitToList(" hello|world |||");
        assertThat(result.get(0), equalTo("hello"));
        assertThat(result.get(1), equalTo("world"));
    }

    /**
     * 固定长度 字符串 分隔
     */
    @Test
    public void testSplitFixLength() {
        List<String> result = Splitter.fixedLength(4)
                .splitToList("aaaabbbbccccdddd");
        assertThat(result.size(), equalTo(4));
        assertThat(result.get(0), equalTo("aaaa"));
        assertThat(result.get(3), equalTo("dddd"));
    }

    /**
     * 根据分隔符，分割成 n 个字符串
     */
    @Test
    public void testSplitOnSplitLimit() {
        List<String> result = Splitter.on("#").limit(3)
                .splitToList("hello#world#java#google#scala");
        System.out.println(result);
        assertThat(result.size(), equalTo(3));
        assertThat(result.get(0), equalTo("hello"));
        assertThat(result.get(1), equalTo("world"));
        assertThat(result.get(2), equalTo("java#google#scala"));
    }

    /**
     * 正则表达式 分隔 字符串
     */
    @Test
    public void tetSplitOnPatternString() {
        List<String> result = Splitter.onPattern("\\|")
                .trimResults().omitEmptyStrings()
                .splitToList("Hello | World||||");
        assertThat(result.size(), equalTo(2));
        assertThat(result.get(0), equalTo("Hello"));
        assertThat(result.get(1), equalTo("World"));
    }

    @Test
    public void tetSplitOnPattern() {
        List<String> result = Splitter.on(Pattern.compile("\\|"))
                .trimResults().omitEmptyStrings()
                .splitToList("Hello | World||||");
        assertThat(result.size(), equalTo(2));
        assertThat(result.get(0), equalTo("Hello"));
        assertThat(result.get(1), equalTo("World"));
    }

    /**
     * 字符串  根据指定分隔符 分隔成 Map
     */
    @Test
    public void tetSplitOnSplitToMap() {
        Map<String, String> result = Splitter.on(Pattern.compile("\\|"))
                .trimResults().omitEmptyStrings()
                .withKeyValueSeparator("=")
                .split("hello=HELLO | world=WORLD|||");
        assertThat(result.size(), equalTo(2));
        assertThat(result.get("hello"), equalTo("HELLO"));
        assertThat(result.get("world"), equalTo("WORLD"));
    }
}

package com.wang.guava.utilities;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @description: Strings 工具类 api 用例
 * @date: 2020/7/25 17:01
 * @author: wei·man cui
 */
public class StringsTest {

    /**
     * Strings.emptyToNull()：将空字符串转换成 null。
     * Strings.nullToEmpty()：将 null转换成 空字符串；若不为null，则原值返回。
     * Strings.commonPrefix()/commonSuffix()：返回多个字符串 的公共前缀 / 公共后缀
     * Strings.repeat(string, n)：将字符串 string 重复3次，拼接后返回
     * Strings.isNullOrEmpty(string)：字符串是否为空字符串或者null
     * Strings.padStart(string,length,fixChar)：字符串长度达不到length时，前面使用Char补充
     * Strings.padEnd(string,length,fixChar)：字符串长度达不到length时，后面使用Char补充
     */
    @Test
    public void testStringsMethod() {
        Assert.assertThat(Strings.emptyToNull(""), Matchers.nullValue());
        Assert.assertThat(Strings.nullToEmpty(null), Matchers.equalTo(""));
        Assert.assertThat(Strings.nullToEmpty("hello"), Matchers.equalTo("hello"));
        Assert.assertThat(Strings.commonPrefix("Hello", "Heat"), Matchers.equalTo("He"));
        Assert.assertThat(Strings.commonSuffix("Hello", "Echo"), Matchers.equalTo("o"));
        Assert.assertThat(Strings.repeat("Alex", 3), Matchers.equalTo("AlexAlexAlex"));
        Assert.assertThat(Strings.isNullOrEmpty(""), Matchers.equalTo(true));
        Assert.assertThat(Strings.padStart("Alex", 3, 'H'), Matchers.equalTo("Alex"));
        Assert.assertThat(Strings.padStart("Alex", 7, 'H'), Matchers.equalTo("HHHAlex"));
        Assert.assertThat(Strings.padEnd("Alex", 5, 'H'), Matchers.equalTo("AlexH"));
    }

    @Test
    public void testCharsets() {
        Charset charset = StandardCharsets.UTF_8;
        Assert.assertThat(Charsets.UTF_8, Matchers.equalTo(charset));
    }

    @Test
    public void testCharMatcher() {
        Assert.assertThat(CharMatcher.javaDigit().matches('5'), Matchers.equalTo(true));
        Assert.assertThat(CharMatcher.javaDigit().matches('x'), Matchers.equalTo(false));
        // 字符串中 有多少个 大写的 A
        Assert.assertThat(CharMatcher.is('A').countIn("Alex Sharing the Google Guava to Us"), Matchers.equalTo(1));
        // 将字符串中的 空格 替换成 指定 符号
        Assert.assertThat(CharMatcher.breakingWhitespace().collapseFrom("     hello guava         ", '*'), Matchers.equalTo("*hello*guava*"));
        Assert.assertThat(CharMatcher.javaDigit().or(CharMatcher.whitespace()).removeFrom("Hello 234 World JustTest 56"), Matchers.equalTo("HelloWorldJustTest"));
    }

}

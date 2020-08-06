package com.wang.guava.io;

import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @description: CharSource 字符流 读取 Reader
 * @date: 2020/8/5 23:32
 * @author: wei·man cui
 */
public class CharSourceTest {

    /**
     * CharSource 基本方法
     */
    @Test
    public void testCharSourceWrap() throws IOException {
        CharSource charSource = CharSource.wrap("i me the CharSource");
        String read = charSource.read();
        assertThat(read, equalTo("i me the CharSource"));
        ImmutableList<String> lines = charSource.readLines();
        assertThat(lines.size(), equalTo(1));
        assertThat(charSource.length(), equalTo(19L));
        assertThat(charSource.lengthIfKnown().get(), equalTo(19L));
    }

    @Test
    public void testConcat() throws IOException {
        CharSource charSource = CharSource.concat(
                CharSource.wrap("i me the CharSource 1"),
                CharSource.wrap("i me the CharSource 2"),
                CharSource.wrap("i me the CharSource 3")
        );
        charSource.lines().forEach(System.out::println);
    }


}

package com.wang.guava.io;

import com.google.common.io.BaseEncoding;
import org.junit.Test;

/**
 * @description: Base64编码 与 反编码
 * @date: 2020/8/8 17:55
 * @author: wei·man cui
 */
public class BaseEncodingTest {

    /**
     * Base64 编解码
     */
    @Test
    public void testBase64EncodingAndDecode() {
        BaseEncoding baseEncoding = BaseEncoding.base64();
        String encode64 = baseEncoding.encode("hello".getBytes());
        System.out.println(encode64);
        String decode = new String(baseEncoding.decode(encode64));
        System.out.println(decode);
    }

    @Test
    public void testSelfEncoding() {
        String encode64 = Base64.encode("hello");
        System.out.println(encode64);
    }

}

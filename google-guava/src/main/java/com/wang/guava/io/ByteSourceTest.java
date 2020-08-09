package com.wang.guava.io;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * ByteSource ——> InputStream
 * ByteSink ——> OutputStream
 *
 * @description: ByteSource 与 ByteSink 字节流
 * @date: 2020/8/6 20:55
 * @author: wei·man cui
 */
public class ByteSourceTest {

    private final String path = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\image.png";

    // 基本方法，详细方法参见源码
    @Test
    public void testAsByteSource() throws IOException {
        File file = new File(path);
        ByteSource byteSource = Files.asByteSource(file);
        byte[] readBytes = byteSource.read();
        assertThat(readBytes, is(Files.toByteArray(file)));
    }

    @Test
    public void testSliceByteSource() throws IOException {
        ByteSource wrap = ByteSource.wrap(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        ByteSource slice = wrap.slice(5, 5);
        byte[] bytes = slice.read();
        for (byte aByte : bytes) {
            System.out.println(aByte);
        }
    }
}

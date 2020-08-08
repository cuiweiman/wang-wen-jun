package com.wang.guava.io;

import com.google.common.io.ByteSource;
import com.google.common.io.Closer;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @description: Closer关闭文件流
 * @date: 2020/8/8 16:52
 * @author: wei·man cui
 */
public class CloserTest {
    ByteSource byteSource = Files.asByteSource(new File("E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\image.png"));

    @Test
    public void testCloser() throws IOException {
        Closer closer = Closer.create();
        try {
            InputStream in = closer.register(byteSource.openStream());
        } catch (Throwable e) {
            throw closer.rethrow(e);
        } finally {
            closer.close();
        }
    }

    @Test
    public void testBeforeCloseIo() {
        // 关闭IO流 方式一：
        try (InputStream inputStream = byteSource.openStream()) {
            inputStream.read("InputStream自动关闭".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 关闭IO流 方式二：
        InputStream inputStream = null;
        try {
            byteSource.openStream();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

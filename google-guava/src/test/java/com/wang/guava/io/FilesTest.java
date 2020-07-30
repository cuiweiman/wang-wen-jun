package com.wang.guava.io;

import com.google.common.io.Files;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @description: Files工具类
 * @date: 2020/7/30 22:56
 * @author: wei·man cui
 */
public class FilesTest {
    private final String SOURCE_FILE = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\source.txt";
    private final String target_file = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\target.txt";

    /**
     * 文件拷贝（推荐字节流，字符流会涉及编码问题）
     * 验证拷贝到是该文件，可以进行 md5签名验证
     */
    @Test
    public void copyFileWithGuava() throws IOException {
        File target = new File(target_file);
        Files.copy(new File(SOURCE_FILE), target);
        assertThat(target.exists(), equalTo(true));
    }

    /**
     * Java NIO 文件拷贝
     */
    @Test
    public void copyFileWithJdkNio2() throws IOException {
        java.nio.file.Files.copy(
                Paths.get("E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources", "io", "source.txt"),
                Paths.get("E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources", "io", "target.txt"),
                StandardCopyOption.REPLACE_EXISTING);
        assertThat(new File(target_file).exists(), equalTo(true));
    }


    @After
    public void tearDown() {
        File file = new File(target_file);
        if (file.exists()) {
            file.delete();
        }
    }
}

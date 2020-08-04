package com.wang.guava.io;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

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
        File source = new File(SOURCE_FILE);
        Files.copy(source, target);
        assertThat(target.exists(), equalTo(true));
        HashCode sourceHashCode = Files.asByteSource(source).hash(Hashing.sha256());
        HashCode targetHashCode = Files.asByteSource(target).hash(Hashing.sha256());
        assertThat(sourceHashCode.equals(targetHashCode), equalTo(true));
    }

    /**
     * 文件移动（推荐字节流，字符流会涉及编码问题）
     * 验证拷贝到是该文件，可以进行 md5签名验证
     */
    @Test
    public void moveFileWithGuava() throws IOException {
        try {
            File source = new File(SOURCE_FILE);
            File target = new File(target_file);
            Files.move(source, target);
            assertThat(source.exists(), equalTo(false));
            assertThat(target.exists(), equalTo(true));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Files.move(new File(target_file), new File(SOURCE_FILE));
        }
    }

    /**
     * 按行读取 File中的内容
     */
    @Test
    public void testToString() throws IOException {
        final String expectedString = "hello Sun YY , today we will share the guava io knowledge.\n" +
                "but only for the basic usage.\n" +
                "if you wanted to get the more details information,\n" +
                "please read the guava document or source code.";
        List<String> strings = Files.readLines(new File(SOURCE_FILE), Charsets.UTF_8);
        String result = Joiner.on("\n").join(strings);
        assertThat(result, equalTo(expectedString));
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

    /**
     * LineProcessor 读取文件内容，并按行处理
     */
    @Test
    public void testToProcessString() throws IOException {
        LineProcessor<List<Integer>> lineProcessor = new LineProcessor<List<Integer>>() {
            private final List<Integer> list = Lists.newArrayList();

            // 58, 29, 50, 46
            @Override
            public boolean processLine(String line) throws IOException {
                if (line.length() == 50) {
                    // 读到 长度等于50，就不会在往下读了
                    return false;
                }
                list.add(line.length());
                return true;
            }

            @Override
            public List<Integer> getResult() {
                return list;
            }
        };
        List<Integer> result = Files.asCharSource(new File(SOURCE_FILE), Charsets.UTF_8)
                .readLines(lineProcessor);
        System.out.println(result);
    }

    /**
     * File Hash Sha算法
     */
    @Test
    public void testFileSha() throws IOException {
        File file = new File(SOURCE_FILE);
        HashCode hash = Files.asByteSource(file).hash(Hashing.sha256());
        System.out.println(hash);
    }

    /**
     * 文件写入内容
     *
     * @throws IOException
     */
    @Test
    public void testFileWrite() throws IOException {
        final String path = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\testWriteFile.txt";
        File file = new File(path);
        // JVM运行结束时，删除文件
        file.deleteOnExit();
        String content = "content test file write.";
        Files.asCharSink(file, Charsets.UTF_8).write(content);
        String read = Files.asCharSource(file, Charsets.UTF_8).read();
        assertThat(read, equalTo(content));
    }

    /**
     * 文件 增加 内容
     *
     * @throws IOException
     */
    @Test
    public void testFileAppend() throws IOException {
        final String path = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\testWriteFile.txt";
        File file = new File(path);
        // JVM运行结束时，删除文件
        file.deleteOnExit();
        String content = "content test file write.";
        CharSink charSink = Files.asCharSink(file, Charsets.UTF_8, FileWriteMode.APPEND);
        //先写入 content
        charSink.write(content);

        String actually = Files.asCharSource(file, Charsets.UTF_8).read();
        assertThat(actually, equalTo(content));

        // 再写入
        charSink.write("content2");
        actually = Files.asCharSource(file, Charsets.UTF_8).read();
        assertThat(actually, equalTo(content + "content2"));
    }

    /**
     * 创建一个 空文件，类似于linux中的touch命令
     *
     * @throws IOException
     */
    @Test
    public void testTouchFile() throws IOException {
        File touchFile = new File(SOURCE_FILE);
        Files.touch(touchFile);
        assertThat(touchFile.exists(), equalTo(true));
    }

    /**
     * 递归遍历 文件夹与文件
     */
    @Test
    public void testRecursive() {
        String path = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main";
        File file = new File(path);
        List<File> list = Lists.newArrayList();
        this.recursiveList(file, list);
        list.forEach(System.out::println);
    }

    // 文件 递归方法
    private void recursiveList(File root, List<File> fileList) {
        if (root.isHidden()) {
            return;
        }
        /* // 获取到了文件夹、以及文件
        fileList.add(root);
        if (!root.isFile()) {
            File[] files = root.listFiles();
            for (File f : files) {
                recursiveList(f, fileList);
            }
        }*/
        // 只获取 文件
        if (root.isFile()) {
            fileList.add(root);
        } else {
            File[] files = root.listFiles();
            for (File f : files) {
                recursiveList(f, fileList);
            }
        }
    }

    /**
     * 遍历 文件夹与文件：广度优先遍历 与 深度优先遍历
     */
    @Test
    public void testFileTraverser() {
        String path = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main";
        File file = new File(path);
        System.out.println("广度优先遍历");
        Iterable<File> files = Files.fileTraverser().breadthFirst(file);
        // 过滤 掉文件夹，打印文件路径
        files.forEach(f -> {
            if (f.isFile()) {
                System.out.println(f);
            }
        });
        System.out.println("深度优先遍历");
        files = Files.fileTraverser().depthFirstPreOrder(file);
        files.forEach(System.out::println);
    }

    @After
    public void tearDown() {
        File file = new File(target_file);
        if (file.exists()) {
            file.delete();
        }
    }
}

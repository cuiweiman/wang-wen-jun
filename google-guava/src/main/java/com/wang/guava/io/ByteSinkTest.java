package com.wang.guava.io;

import com.google.common.hash.Hashing;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @description: ByteSink 字节流 OutputStream
 * @date: 2020/8/6 21:52
 * @author: wei·man cui
 */
public class ByteSinkTest {
    private final String path = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\ByteSinkTest.txt";

    @Test
    public void testByteSink() throws IOException {
        File file = new File(path);
        file.deleteOnExit();
        ByteSink byteSink = Files.asByteSink(file);
        byte[] bytes = new byte[]{0x01, 0x02};
        byteSink.write(bytes);

        byte[] expected = Files.toByteArray(file);
        assertThat(expected, is(bytes));
    }

    @Test
    public void tstFromSourceToSink() throws IOException {
        String source = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\image.png";
        String target = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\image_copy.png";
        File sourceFile = new File(source);
        ByteSource byteSource = Files.asByteSource(sourceFile);

        File targetFile = new File(target);
        targetFile.deleteOnExit();
        byteSource.copyTo(Files.asByteSink(targetFile));
        assertThat(targetFile.exists(), equalTo(true));

        assertThat(
                Files.asByteSource(sourceFile).hash(Hashing.sha256()).toString(),
                equalTo(Files.asByteSource(targetFile).hash(Hashing.sha256()).toString())
        );
    }
}

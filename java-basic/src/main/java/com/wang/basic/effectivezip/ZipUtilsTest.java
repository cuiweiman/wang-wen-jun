package com.wang.basic.effectivezip;

import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @description: 校验压缩
 * @author: wei·man cui
 * @date: 2021/2/24 10:41
 */
public class ZipUtilsTest {

    private File source = new File("F:\\upload");
    String targetPath = "F:/upload_" + System.currentTimeMillis() + ".zip";
    private File target = new File(targetPath);

    @Test
    public void testZipUtilParallelScatter() {
        long start = System.currentTimeMillis();
        ZipUtilParallelScatter zipUtil = new ZipUtilParallelScatter();
        final String zipPath = zipUtil.doZipFile(source, target);
        System.out.println("耗时：" + (System.currentTimeMillis() - start) + "；压缩文件路径：" + zipPath);
    }


}

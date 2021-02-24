package com.wang.basic.effectivezip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.io.input.NullInputStream;

import java.io.*;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;

/**
 * @description: 文件压缩工具类
 * @author: wei·man cui
 * @date: 2021/2/24 10:05
 */
public class ZipUtilParallelScatter {

    /**
     * 压缩
     *
     * @param source  将要被压缩的文件
     * @param zipFile 压缩后的文件
     * @return 压缩后的文件路径
     */
    public String doZipFile(File source, File zipFile) {
        try {
            File zipFileParent = new File(zipFile.getParent());
            // 若 压缩文件的路径 不存在，则创建出来
            if (!zipFileParent.exists()) {
                zipFileParent.mkdirs();
            }
            ScatterSample scatterSample = new ScatterSample(source.getAbsolutePath());
            compressCurrentDirectory(scatterSample, source);
            ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(zipFile);
            scatterSample.writeTo(zipArchiveOutputStream);
            zipArchiveOutputStream.close();

            return zipFile.getAbsolutePath();
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void compressCurrentDirectory(ScatterSample scatterSample, File source) throws IOException {
        if (source == null) {
            throw new IOException("源路径不能为空！");
        }
        String relativePath = "";
        if (source.isFile()) {
            relativePath = source.getName();
            addEntry(relativePath, source, scatterSample);
            return;
        }

        // 空文件夹
        if (Objects.requireNonNull(source.listFiles()).length == 0) {
            relativePath = source.getAbsolutePath().replace(scatterSample.getRootPath(), "");
            addEntry(relativePath + File.separator, source, scatterSample);
            return;
        }
        for (File f : Objects.requireNonNull(source.listFiles())) {
            if (f.isDirectory()) {
                compressCurrentDirectory(scatterSample, f);
            } else {
                relativePath = f.getParent().replace(scatterSample.getRootPath(), "");
                addEntry(relativePath + File.separator + f.getName(), f, scatterSample);
            }
        }
    }

    private void addEntry(String entryName, File currentFile, ScatterSample scatterSample) throws IOException {
        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(entryName);
        archiveEntry.setMethod(ZipEntry.DEFLATED);
        final InputStreamSupplier supp = new CustomInputStreamSupplier(currentFile);
        scatterSample.addEntry(archiveEntry, supp);
    }

    class CustomInputStreamSupplier implements InputStreamSupplier {
        private File currentFile;

        public CustomInputStreamSupplier(File currentFile) {
            this.currentFile = currentFile;
        }

        @Override
        public InputStream get() {
            try {
                // InputStreamSupplier api says:
                // 返回值：输入流。永远不能为Null,但可以是一个空的流
                return currentFile.isDirectory() ? new NullInputStream(0) : new FileInputStream(currentFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}

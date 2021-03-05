package com.wang.basic.io;

import com.google.common.base.Splitter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 2021最新JAVA面试题200集（真题必问），1个月内我靠这个《java面试宝典》从13K到大厂5 - 1.【性能调优专题】JVM调优-JDK体系结构与jvm整体架构解析(Av886819979,P1).mp4
 * </p>
 *
 * @description: jjDownload 下载的文件修改文件名
 * @author: wei·man cui
 * @date: 2021/3/5 10:37
 */
public class FilesRename {
    public static void main(String[] args) {
        String path = "F:/资料";
        File file = new File(path);
        final File[] files = file.listFiles();
        assert files != null;
        Arrays.stream(files).forEach(item -> {
            if (item.getName().contains(".mp4")) {
                String oldName = item.getName();
                String name = oldName.replace("2021最新JAVA面试题200集（真题必问），1个月内我靠这个《java面试宝典》从13K到大厂5 - ", "");
                name = name.replace("【", "[").replace("】", "]");
                name = name.replace("Av886819979,", "");
                final List<String> nameList = Splitter.on(".").splitToList(name);
                String num = nameList.get(0);
                num = String.format("%02d", Integer.valueOf(num));

                String realName = num.concat(".").concat(nameList.get(1)).concat(".").concat(nameList.get(2));
                File target = new File(path.concat("/").concat(realName));
                final boolean b = item.renameTo(target);
                System.out.println("文件重命名是否成功：" + b + "  " + realName);
            }
        });


    }
}

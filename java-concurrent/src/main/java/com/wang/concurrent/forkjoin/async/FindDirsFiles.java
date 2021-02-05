package com.wang.concurrent.forkjoin.async;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * 使用Fork-Join框架，异步方式，找寻指定类型文件
 */
public class FindDirsFiles extends RecursiveAction {

    // 文件目录
    private File path;

    public FindDirsFiles(File path) {
        this.path = path;
    }

    @Override
    protected void compute() {
        List<FindDirsFiles> subTasks = new ArrayList<>();
        File[] files = path.listFiles();
        if (files != null) {
            // 遍历 目录下的 所有文件
            for (File file : files) {
                // 若是文件，则 进入递归调用
                if (file.isDirectory()) {
                    subTasks.add(new FindDirsFiles(file));
                } else {
                    // 若是文件，则判断是否符合指定文件的规则
                    if (file.getAbsolutePath().endsWith("txt")) {
                        System.out.println("文件：" + file.getAbsolutePath());
                    }
                }
            }
        }
        if (!subTasks.isEmpty()) {
            // 提交所有的子任务，并集合所有子任务的执行结果
            for (FindDirsFiles subTask : invokeAll(subTasks)) {
                subTask.join();
            }
        }
    }

    public static void main(String[] args) {
        try {
            ForkJoinPool pool = new ForkJoinPool();
            FindDirsFiles task = new FindDirsFiles(new File("D:/"));

            // 异步调用
            pool.execute(task);

            System.out.println("Task is running……");
            Thread.sleep(1);
            int otherWork = 0;
            for (int i = 0; i < 100; i++) {
                otherWork = otherWork + i;
            }
            System.out.println("Main Thread done sth……, otherWork=" + otherWork);
            // 阻塞方法，等待 异步线程task执行完成。
            task.join();
            System.out.println("Task end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

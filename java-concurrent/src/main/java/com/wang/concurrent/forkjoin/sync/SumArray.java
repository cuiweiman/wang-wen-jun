package com.wang.concurrent.forkjoin.sync;


import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 使用Fork-Join方法，同步方式 计算数组的元素之和
 * 多线程框架 由于CPU上下文切换，运行时间可能不比单线程运行时间快
 */
public class SumArray {

    private static class SumTask extends RecursiveTask<Integer> {
        // 拆分阈值
        private final static int THRESHOLD = MakeArrayUtil.ARRAY_LENGTH / 10;

        // 数组
        private int[] arr;

        // 开始下标
        private int fromIndex;

        // 结束下标
        private int toIndex;

        public SumTask(int[] arr, int fromIndex, int toIndex) {
            this.arr = arr;
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
        }

        @Override
        protected Integer compute() {
            // 满足设定条件，计算元素之和
            if (toIndex - fromIndex < THRESHOLD) {
                int count = 0;
                for (int i = fromIndex; i <= toIndex; i++) {
                    // SleepTools.ms(1);
                    count += arr[i];
                }
                return count;
            } else {
                // 不满足设定条件，递归拆分
                int mid = (fromIndex + toIndex) / 2;
                SumTask left = new SumTask(arr, fromIndex, mid);
                SumTask right = new SumTask(arr, mid + 1, toIndex);
                invokeAll(left, right);
                return left.join() + right.join();
            }
        }
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        int[] arr = MakeArrayUtil.makeArray();
        SumTask innerFind = new SumTask(arr, 0, arr.length - 1);
        long start = System.currentTimeMillis();

        // 同步调用
        pool.invoke(innerFind);

        System.out.println("Task is Running......");
        System.out.println("Count = " + innerFind.join()
                + "  SpendTime=" + (System.currentTimeMillis() - start));
    }

}

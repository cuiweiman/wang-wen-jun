package com.wang.concurrent.forkjoin.sync;

/**
 * 对照方法
 * 使用普通方法，计算数组元素之和
 */
public class SumNormal {

    public static void main(String[] args) {
        int count = 0;
        int[] arr = MakeArrayUtil.makeArray();
        long start = System.currentTimeMillis();
        for (int i = 0; i < arr.length; i++) {
            // SleepTools.ms(1);
            count += arr[i];
        }
        System.out.println("count=" + count);
        System.out.println("普通方法耗时：" + (System.currentTimeMillis() - start));
    }

}

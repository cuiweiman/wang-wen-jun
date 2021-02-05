package com.wang.concurrent.forkjoin.sync;

import java.util.Random;

/**
 * 整型数组 生成 工具列
 */
public class MakeArrayUtil {

    public static final int ARRAY_LENGTH = 4000;

    public static int[] makeArray() {
        Random r = new Random();
        int[] result = new int[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            result[i] = r.nextInt(ARRAY_LENGTH * 3);
        }
        return result;
    }

}

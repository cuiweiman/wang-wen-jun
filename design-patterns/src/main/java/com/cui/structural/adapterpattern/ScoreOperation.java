package com.cui.structural.adapterpattern;

/**
 * @description: 抽象成绩操作类，目标接口
 * @author: wei·man cui
 * @date: 2020/7/10 9:36
 */
public interface ScoreOperation {

    /**
     * 快速排序：成绩排序
     *
     * @param array
     * @return
     */
    int[] sort(int[] array);

    /**
     * 二分查找：成绩查找
     *
     * @param array
     * @param key
     * @return
     */
    int search(int[] array, int key);

}

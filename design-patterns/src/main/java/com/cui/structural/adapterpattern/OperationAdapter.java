package com.cui.structural.adapterpattern;

/**
 * @description: 操作适配器：适配器
 * @author: weiman cui
 * @date: 2020/7/10 10:21
 */
public class OperationAdapter implements ScoreOperation {
    /**
     * 定义适配者QuickSort对象
     */
    private QuickSort sortObj;

    /**
     * 定义适配者BinarySearch对象
     */
    private BinarySearch searchObj;

    public OperationAdapter() {
        sortObj = new QuickSort();
        searchObj = new BinarySearch();
    }

    /**
     * 调用适配者类QuickSort的排序方法
     *
     * @param array
     * @return
     */
    @Override
    public int[] sort(int[] array) {
        return sortObj.quickSort(array);
    }

    /**
     * 调用适配者类BinarySearch的查找方法
     *
     * @param array
     * @param key
     * @return
     */
    @Override
    public int search(int[] array, int key) {
        return searchObj.binarySearch(array, key);
    }
}


package com.wang.binarytree;

import java.util.Arrays;

/**
 * @description: 堆 排序
 * @date: 2021/3/4 23:05
 * @author: wei·man cui
 */
public class AcHeapSort {

    public static void main(String[] args) {
        int[] arr = {9, 6, 8, 7, 0, 1, 10, 4, 2};
        // 开始节点 应该是 最后一个 非叶子节点（最后一个节点的父节点）
        heapSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void heapSort(int[] arr) {
        int start = (arr.length - 1) / 2;
        for (int i = start; i >= 0; i--) {
            maxHeap(arr, arr.length, i);
        }
        for (int i = arr.length - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            maxHeap(arr, i, 0);
        }
    }

    public static void maxHeap(int[] arr, int length, int start) {
        // 左子节点
        int left = 2 * start + 1;
        // 右子节点
        int right = 2 * start + 2;
        // 三个节点对比，找出最大的值
        int max = start;
        if (left < length && arr[left] > arr[max]) {
            max = left;
        }
        if (right < length && arr[right] > arr[max]) {
            max = right;
        }
        // 交换位置
        if (max != start) {
            int temp = arr[start];
            arr[start] = arr[max];
            arr[max] = temp;
            maxHeap(arr, length, max);
        }
    }

}

package com.wang.binarytree;

/**
 * 1. 顺序存储二叉树，对顺序存储二叉树从上到下，从左到右进行层次遍历，可以得到一个顺序数组。通常只考虑完全二叉树。
 * 2. 顺序存储二叉树中，第 n 个元素的左子节点在数组中的下标是：2n+1，右子节点的下标是：2n+2。
 * 3. 第 n 个元素的 父节点，在数组中的下标是：(n-1)/2。
 *
 * @description: 顺序存储二叉树
 * @date: 2021/3/4 22:33
 * @author: wei·man cui
 */
public class AbArrayBinaryTree {

    public static void main(String[] args) {
        AbArrayBinaryTree tree = new AbArrayBinaryTree(new int[]{5, 6, 3, 8, 2, 1, 7});
        // tree.frontShow(0);
        // tree.midShow(0);
        tree.afterShow(0);
    }


    int[] data;

    public AbArrayBinaryTree(int[] data) {
        this.data = data;
    }

    /**
     * 先序遍历
     */
    public void frontShow(int index) {
        System.out.println(data[index]);
        if (2 * index + 1 < data.length) {
            frontShow(2 * index + 1);
        }
        if (2 * index + 2 < data.length) {
            frontShow(2 * index + 2);
        }
    }

    /**
     * 中序遍历
     */
    public void midShow(int index) {
        if (2 * index + 1 < data.length) {
            midShow(2 * index + 1);
        }
        System.out.println(data[index]);
        if (2 * index + 2 < data.length) {
            midShow(2 * index + 2);
        }
    }

    /**
     * 后序遍历
     */
    public void afterShow(int index) {
        if (2 * index + 1 < data.length) {
            afterShow(2 * index + 1);
        }
        if (2 * index + 2 < data.length) {
            afterShow(2 * index + 2);
        }
        System.out.println(data[index]);
    }

}

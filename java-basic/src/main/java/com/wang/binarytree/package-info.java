/**
 * 1. 普通二叉树，链式存储：{@link com.wang.binarytree.AaBinaryTree}
 * 2. 顺序存储二叉树（考虑完全二叉树），数组存储，层次遍历：{@link com.wang.binarytree.AbArrayBinaryTree}
 * 3. 堆结构（堆排序）：对于任何一个子树，父节点永远大于/小于子节点。（大顶堆：父节点>子节点；小顶堆：父节点<子节点。）
 * 堆排序可以看做：将平衡二叉树，转换成一个大顶堆或者小顶堆：{@link com.wang.binarytree.AcHeapSort}
 * 大顶堆的根节点最大，交换根节点和叶子节点，然后移除根节点（这样才能保证树的节点不丢失），再将子树转换成大顶堆，得出第二个最大的数，依次循环。
 *
 * @description: 二叉树
 * @date: 2021/3/4 21:33
 * @author: wei·man cui
 */
package com.wang.binarytree;
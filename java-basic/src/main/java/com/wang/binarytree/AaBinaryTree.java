package com.wang.binarytree;

/**
 * @description: 简单二叉树
 * @date: 2021/3/4 21:35
 * @author: wei·man cui
 */
public class AaBinaryTree {

    public static void main(String[] args) {
        BinaryNode tree = createTree();
        // tree.frontShow();
        // tree.midShow();
        // tree.afterShow();
        // TreeNode searchNode = tree.frontSearch(1);
        // System.out.println(searchNode.getValue());
        tree.remove(6);
        tree.frontShow();
    }

    public static BinaryNode createTree() {
        BinaryNode root = new BinaryNode(5);
        BinaryNode rootL = root.setLeft(new BinaryNode(6));
        BinaryNode rootR = root.setRight(new BinaryNode(3));
        BinaryNode rootLL = rootL.setLeft(new BinaryNode(8));
        rootL.setRight(new BinaryNode(2));
        rootLL.setLeft(new BinaryNode(9));
        BinaryNode rootRL = rootR.setLeft(new BinaryNode(1));
        rootR.setRight(new BinaryNode(7));
        rootRL.setRight(new BinaryNode(6));
        return root;
    }

}

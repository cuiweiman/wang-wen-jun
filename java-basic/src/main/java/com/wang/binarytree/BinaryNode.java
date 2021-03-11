package com.wang.binarytree;

import lombok.Data;

import java.util.Objects;

/**
 * @description: 普通二叉树节点
 * @date: 2021/3/4 21:37
 * @author: wei·man cui
 */
@Data
public class BinaryNode {

    private Integer value;

    private BinaryNode left;

    private BinaryNode right;

    public BinaryNode setLeft(BinaryNode left) {
        this.left = left;
        return this.left;
    }

    public BinaryNode setRight(BinaryNode right) {
        this.right = right;
        return this.right;
    }

    public BinaryNode(Integer value) {
        this.value = value;
    }

    /**
     * 先序遍历
     */
    public void frontShow() {
        System.out.println(value);
        if (Objects.nonNull(left)) {
            left.frontShow();
        }
        if (Objects.nonNull(right)) {
            right.frontShow();
        }
    }

    /**
     * 中序遍历
     */
    public void midShow() {
        if (Objects.nonNull(left)) {
            left.midShow();
        }
        System.out.println(value);
        if (Objects.nonNull(right)) {
            right.midShow();
        }
    }

    /**
     * 后序遍历
     */
    public void afterShow() {
        if (Objects.nonNull(left)) {
            left.afterShow();
        }
        if (Objects.nonNull(right)) {
            right.afterShow();
        }
        System.out.println(value);
    }

    public BinaryNode frontSearch(int target) {
        BinaryNode node = null;
        if (this.value == target) {
            return this;
        } else {
            if (Objects.nonNull(left)) {
                node = left.frontSearch(target);
            }
            if (Objects.nonNull(node)) {
                return node;
            }
            if (Objects.nonNull(right)) {
                node = right.frontSearch(target);
            }
        }
        return node;
    }

    public void remove(int target) {
        BinaryNode parent = this;
        if (Objects.nonNull(parent.left) && parent.left.getValue() == target) {
            parent.left = null;
        }
        if (Objects.nonNull(parent.right) && parent.right.getValue() == target) {
            parent.right = null;
        }
        // 左右儿子都不是，那么要递归遍历
        parent = left;
        if (Objects.nonNull(parent)) {
            parent.remove(target);
        }
        parent = right;
        if (Objects.nonNull(parent)) {
            parent.remove(target);
        }
    }

}

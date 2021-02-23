package com.wang.boot.limiting.enums;

/**
 * @description: 限流 类型 枚举
 * @author: wei·man cui
 * @date: 2021/2/22 17:45
 */
public enum GlobalLimitType {
    /**
     * 传统类型
     */
    CUSTOMER,
    /**
     *  根据 IP地址限制
     */
    IP
}
package com.wang.concurrent.readwritelock;

import lombok.Data;

/**
 * 读写锁演示——商品实体类
 */
@Data
public class GoodsInfo {
    private final String name;
    // 商品库存
    private double totalMoney;
    // 销售数量
    private int storeNumber;

    public GoodsInfo(String name, double totalMoney, int storeNumber) {
        this.name = name;
        this.totalMoney = totalMoney;
        this.storeNumber = storeNumber;
    }

    public void changeNumber(int sellNumber) {
        this.totalMoney += sellNumber * 25;
        this.storeNumber -= sellNumber;
    }
}

package com.wang.concurrent.deadlock.bank;

import lombok.Data;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 模拟 银行转账 实体类
 *
 * @author weiman cui
 * @date 2020/5/17 18:01
 */
@Data
public class UserAccount {

    private final String name;

    private int money;

    private final Lock lock = new ReentrantLock();

    public UserAccount(String name, int money) {
        this.name = name;
        this.money = money;
    }

    /**
     * 转入资金
     *
     * @param amount
     */
    public void addMoney(int amount) {
        money += amount;
    }

    /**
     * 转出资金
     *
     * @param amount
     */
    public void flyMoney(int amount) {
        money -= amount;
    }

}

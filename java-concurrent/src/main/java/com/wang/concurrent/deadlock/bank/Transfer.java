package com.wang.concurrent.deadlock.bank;

/**
 * 银行转账动作 接口
 *
 * @author weiman cui
 * @date 2020/5/17 18:08
 */
public interface Transfer {

    /**
     * 转账操作 动作实现
     *
     * @param from   转出账户
     * @param to     转入账户
     * @param amount 金额
     * @throws InterruptedException
     */
    void transferAccount(UserAccount from,
                         UserAccount to,
                         int amount) throws InterruptedException;

}

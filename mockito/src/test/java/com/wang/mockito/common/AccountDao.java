package com.wang.mockito.common;

/**
 * @description: 用户账号 dao
 * @author: wei·man cui
 * @date: 2020/8/13 10:41
 */
public class AccountDao {

    public Account findAccount(String uerName, String password) {
        // DB是不可用的
        throw new UnsupportedOperationException();
    }
}

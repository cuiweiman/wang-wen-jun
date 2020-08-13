package com.wang.mockito.lesson03.mockmethod;

import com.wang.mockito.common.Account;
import com.wang.mockito.common.AccountDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @description: 使用Mockito启动器进行单元测试
 * @author: wei·man cui
 * @date: 2020/8/13 11:08
 */
@RunWith(MockitoJUnitRunner.class)
public class MockitoByRunnerTest {

    @Test
    public void testMock() {
        // AccountDao accountDao = Mockito.mock(AccountDao.class);
        AccountDao accountDao = Mockito.mock(AccountDao.class, Mockito.RETURNS_SMART_NULLS);
        // 不会报错，但返回null。不会真正执行 accountDao.findAccount()方法，只是模拟该行为。
        Account account = accountDao.findAccount("x", "y");
        System.out.println(account);
    }

}

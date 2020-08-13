package com.wang.mockito.lesson03.mockmethod;

import com.wang.mockito.common.Account;
import com.wang.mockito.common.AccountDao;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * @description: Rule方式 进行Mock
 * @author: wei·man cui
 * @date: 2020/8/13 11:26
 */
public class MockByRuleTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private AccountDao accountDao;

    @Test
    public void tetMock() {
        // AccountDao accountDao = Mockito.mock(AccountDao.class, Mockito.CALLS_REAL_METHODS);
        Account account = accountDao.findAccount("x", "y");
        System.out.println(account);
    }

}

package com.wang.mockito.quickstart;

import com.wang.mockito.common.Account;
import com.wang.mockito.common.AccountDao;
import com.wang.mockito.common.AccountLoginController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;

/**
 * Lesson02： Mockito快速入门 Demo
 *
 * @description: AccountLoginController Mockito单源测试
 * @author: wei·man cui
 * @date: 2020/8/13 10:45
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountLoginControllerTest {

    private AccountDao accountDao;

    private HttpServletRequest request;

    private AccountLoginController accountLoginController;

    @Before
    public void setUp() {
        this.accountDao = Mockito.mock(AccountDao.class);
        this.request = Mockito.mock(HttpServletRequest.class);
        this.accountLoginController = new AccountLoginController(accountDao);
    }

    @Test
    public void login() {
        Account account = new Account();
        Mockito.when(request.getParameter("userName")).thenReturn("Sun");
        Mockito.when(request.getParameter("password")).thenReturn("123456");
        Mockito.when(accountDao.findAccount(anyString(), anyString())).thenReturn(account);
        assertThat(accountLoginController.login(request), equalTo("index"));
    }

    @Test
    public void login505() {
        Mockito.when(request.getParameter("userName")).thenReturn("Sun");
        Mockito.when(request.getParameter("password")).thenReturn("123456");
        Mockito.when(accountDao.findAccount(anyString(), anyString())).thenThrow(UnsupportedOperationException.class);
        assertThat(accountLoginController.login(request), equalTo("505"));
    }
}








package com.wang.mockito.lesson03.mockmethod;

import com.wang.mockito.common.Account;
import com.wang.mockito.common.AccountDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @description: Annotation方式进行Mock
 * @author: wei·man cui
 * @date: 2020/8/13 11:22
 */
public class MockByAnnotationTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    private AccountDao accountDao;

    @Test
    public void testMock() {
        Account account = accountDao.findAccount("x", "y");
        System.out.println(account);
    }

}

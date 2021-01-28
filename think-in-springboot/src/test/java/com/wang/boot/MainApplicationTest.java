package com.wang.boot;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/1/28 17:44
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MainApplicationTest {

    @Test
    public void test() {
        Assert.assertTrue(Boolean.TRUE);
    }

}

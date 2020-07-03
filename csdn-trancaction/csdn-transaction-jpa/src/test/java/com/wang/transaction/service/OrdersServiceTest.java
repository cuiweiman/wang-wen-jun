package com.wang.transaction.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @description:
 * @author: weiman cui
 * @date: 2020/7/3 10:44
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrdersServiceTest {

    @Resource
    private OrdersService service;

    @Before
    public void deleteAll() {
        service.deleteAll();
    }

    @Test
    public void saveOrder() {
        service.saveOrder(9999L);
    }
}
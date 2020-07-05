package com.wang.transaction.mul.service;

import com.wang.transaction.mul.orders.service.MulOrdersService;
import com.wang.transaction.mul.stores.service.MulStoresService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @description:
 * @date: 2020/7/5 11:51
 * @author: weiman cui
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MulMulOrdersServiceTest {

    @Resource
    private MulOrdersService mulOrdersService;

    @Resource
    private MulStoresService mulStoresService;

    @Before
    public void deleteAll() {
        mulOrdersService.deleteAll();
        mulStoresService.deleteAll();
    }

    @Test
    public void saveOrder() {
        mulOrdersService.saveOrderWithNoTransactionManager();
    }
}
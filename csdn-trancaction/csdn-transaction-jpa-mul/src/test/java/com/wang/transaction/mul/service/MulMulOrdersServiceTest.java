package com.wang.transaction.mul.service;

import com.wang.transaction.mul.orders.service.MulOrdersService;
import com.wang.transaction.mul.stores.service.MulStoresService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @description:
 * @date: 2020/7/5 11:51
 * @author: wei·man cui
 */
@SpringBootTest
public class MulMulOrdersServiceTest {

    @Resource
    private MulOrdersService mulOrdersService;

    @Resource
    private MulStoresService mulStoresService;

    @BeforeAll
    public void deleteAll() {
        mulOrdersService.deleteAll();
        mulStoresService.deleteAll();
    }

    @Test
    public void saveOrder() {
        mulOrdersService.saveOrderWithNoTransactionManager();
    }
}
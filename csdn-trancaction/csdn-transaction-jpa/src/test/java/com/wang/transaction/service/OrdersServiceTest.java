package com.wang.transaction.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2020/7/3 10:44
 */
@SpringBootTest
public class OrdersServiceTest {

    @Resource
    private OrdersService service;

    @Resource
    private OrdersDetailService detailService;

    @Resource
    private OrderService2 service2;

    @BeforeAll
    public void deleteAll() {
        service.deleteAll();
    }

    @Test
    public void saveOrderRuntimeException() {
        service.saveOrderRuntimeException(9999L);
    }

    @Test
    public void saveOrderException() throws Exception {
        service.saveOrderException(9999L);
    }


    @Test
    public void saveOrderReadOnly() throws Exception {
        service.saveOrderReadOnly(9999L);
    }

    @Test
    public void saveOrderTimeout() throws Exception {
        service.saveOrderTimeout(9999L);
    }

    /**
     * 被调用方法  事务是否仍然有效
     */
    @Test
    public void testSaveOrder() {
        // 不同类中，被调用方法 是 仍然有效的
        // service2.saveOrder();
        // 统一各类中，被调用方法的 事务 失效
        service.saveOrder();
    }

    /**
     * 事务测试
     */
    @Test
    public void saveOrderPropagation() {
        service.saveOrderPropagation();
    }


    @Test
    public void testException() {
        service.saveOrderRuntimeException2(8888L);
    }

    /**
     * 事务测试：有 事务注解的 方法，调用其他 Service 中没有服务注解的方法，会回滚么？
     * 结论：会回滚
     */
    @Test
    public void testPropagation() {
        service.saveOrderRuntimeException3(9988L);
    }
}
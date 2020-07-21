package com.wang.transaction.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 订单服务，测试事务失效情况
 * @author: wei·man cui
 * @date: 2020/7/3 13:50
 */
@Service
public class OrderService2 {

    @Resource
    private OrdersService service;

    /**
     * 不同类中，调用 事务方法，被调用方法的事务不会失效
     */
    public void saveOrder() {
        service.saveOrderRuntimeException(1000L);
    }

}

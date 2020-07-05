package com.wang.transaction.mul.orders.service;


import com.wang.transaction.mul.orders.entity.MulOrders;
import com.wang.transaction.mul.orders.repository.MulOrdersDetailRepository;
import com.wang.transaction.mul.orders.repository.MulOrdersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @description: 订单服务 单元测试调用
 * @author: weiman cui
 * @date: 2020/7/3 10:37
 */
@Service
public class MulOrdersService {

    @Resource
    private MulOrdersRepository mulOrdersRepository;

    @Resource
    private MulOrdersDetailRepository mulOrdersDetailRepository;

    /**
     * 测试事务的传播机制
     */
    @Resource
    private MulOrdersDetailService detailService;

    public void deleteAll() {
        mulOrdersRepository.deleteAll();
        mulOrdersDetailRepository.deleteAll();
    }

    /**
     * 若 Transactional注解 不配置事务管理器，则在 非事务 情景下运行
     */
    @Transactional(transactionManager = "orderTransactionManager")
    public void saveOrderWithNoTransactionManager() {
        MulOrders mulOrders = new MulOrders();
        mulOrders.setOrderId(555555L);
        mulOrders.setMerchantId(100L);
        mulOrders.setUserId(100L);
        mulOrders.setOrderStatus(0);
        mulOrders.setOrderNote("多数据源事务Orders");
        mulOrdersRepository.save(mulOrders);
        detailService.saveDetail();

        throw new RuntimeException();
    }


}

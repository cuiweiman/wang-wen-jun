package com.wang.transaction.service;

import com.wang.transaction.entity.Orders;
import com.wang.transaction.entity.OrdersDetail;
import com.wang.transaction.repository.OrdersDetailRepository;
import com.wang.transaction.repository.OrdersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @description: 订单服务 单元测试调用
 * @author: wei·man cui
 * @date: 2020/7/3 10:37
 */
@Service
public class OrdersService {

    @Resource
    private OrdersRepository ordersRepository;

    @Resource
    private OrdersDetailRepository ordersDetailRepository;

    /**
     * 测试事务的传播机制
     */
    @Resource
    private OrdersDetailService detailService;

    public void deleteAll() {
        ordersRepository.deleteAll();
        ordersDetailRepository.deleteAll();
    }


    /**
     * Transactional 注解默认回滚异常是 运行时异常RuntimeException
     *
     * @param orderId
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveOrderRuntimeException(Long orderId) {
        Orders orders = new Orders();
        orders.setOrderId(orderId);
        orders.setMerchantId(100L);
        orders.setUserId(100L);
        orders.setOrderStatus(0);
        orders.setOrderNote("订单写入");
        ordersRepository.save(orders);
        OrdersDetail detail = new OrdersDetail();
        detail.setOrderId(orderId);
        detail.setGoodsId(100L);
        detail.setGoodsNote("订单写入-足球");
        ordersDetailRepository.save(detail);
        // 运行时异常，触发事务回滚
        throw new RuntimeException();
    }


    /**
     * 抛出了Exception（检查型异常），但是事务没有回滚。
     * 需要对 Transactional注解 配置 触发事务回滚的异常类型
     *
     * @param orderId
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderException(Long orderId) throws Exception {
        Orders orders = new Orders();
        orders.setOrderId(orderId);
        orders.setMerchantId(100L);
        orders.setUserId(100L);
        orders.setOrderStatus(0);
        orders.setOrderNote("订单写入");
        ordersRepository.save(orders);
        OrdersDetail detail = new OrdersDetail();
        detail.setOrderId(orderId);
        detail.setGoodsId(100L);
        detail.setGoodsNote("订单写入-足球");
        ordersDetailRepository.save(detail);
        // 运行时异常，触发事务回滚
        throw new Exception();
    }

    @Transactional(readOnly = true)
    public void saveOrderReadOnly(Long orderId) {
        Orders orders = new Orders();
        orders.setOrderId(orderId);
        orders.setMerchantId(100L);
        orders.setUserId(100L);
        orders.setOrderStatus(0);
        orders.setOrderNote("订单写入");
        ordersRepository.save(orders);
        OrdersDetail detail = new OrdersDetail();
        detail.setOrderId(orderId);
        detail.setGoodsId(100L);
        detail.setGoodsNote("订单写入-足球");
        ordersDetailRepository.save(detail);
    }

    @Transactional(timeout = 2)
    public void saveOrderTimeout(Long orderId) throws InterruptedException {
        Orders orders = new Orders();
        orders.setOrderId(orderId);
        orders.setMerchantId(100L);
        orders.setUserId(100L);
        orders.setOrderStatus(0);
        orders.setOrderNote("订单写入");
        ordersRepository.save(orders);
        OrdersDetail detail = new OrdersDetail();
        detail.setOrderId(orderId);
        detail.setGoodsId(100L);
        detail.setGoodsNote("订单写入-足球");
        // 构造 请求超时 情况
        // Thread.sleep(2000L);
        ordersDetailRepository.save(detail);
        // 事务已经结束，此处的超时情况 不会产生异常
        Thread.sleep(2000L);
    }

    /**
     * 同类中，调用Transactional修饰的方法，被调用方法的事务会失效
     */
    public void saveOrder() {
        saveOrderRuntimeException(1000L);
    }


    // @Transactional
    // @Transactional
    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    // @Transactional(propagation = Propagation.REQUIRED)
    // 事务A 没开启事务，对应事务B的SUPPORTS
    // 事务A 没开启事务，对应事务B的MANDATORY
    // @Transactional
    // @Transactional
    @Transactional
    public void saveOrderPropagation() {
        Orders orders = new Orders();
        orders.setOrderId(555555L);
        orders.setMerchantId(100L);
        orders.setUserId(100L);
        orders.setOrderStatus(0);
        orders.setOrderNote("订单写入");
        ordersRepository.save(orders);
        detailService.saveDetail();

        // throw new RuntimeException();
    }


}

package com.wang.transaction.service;

import com.wang.transaction.entity.Orders;
import com.wang.transaction.repository.OrdersDetailRepository;
import com.wang.transaction.entity.OrdersDetail;
import com.wang.transaction.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 订单服务
 * @author: weiman cui
 * @date: 2020/7/3 10:37
 */
@Service
public class OrdersService {

    @Resource
    private OrdersRepository ordersRepository;

    @Resource
    private OrdersDetailRepository ordersDetailRepository;

    public void saveOrder(Long orderId) {
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

    public void deleteAll() {
        ordersRepository.deleteAll();
        ordersDetailRepository.deleteAll();
    }

}

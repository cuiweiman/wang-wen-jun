package com.wang.transaction.service;

import com.wang.transaction.entity.OrdersDetail;
import com.wang.transaction.repository.OrdersDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @description: 订单详情服务
 * @author: weiman cui
 * @date: 2020/7/3 14:29
 */
@Service
public class OrdersDetailService {
    @Resource
    private OrdersDetailRepository repository;


    // @Transactional
    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    // @Transactional
    // @Transactional(propagation = Propagation.SUPPORTS)
    // @Transactional(propagation = Propagation.SUPPORTS)
    // @Transactional(propagation = Propagation.MANDATORY)
    // @Transactional(propagation = Propagation.NOT_SUPPORTED)
    // @Transactional(propagation = Propagation.NEVER)
    @Transactional(propagation = Propagation.NESTED)
    public void saveDetail() {
        OrdersDetail detail = new OrdersDetail();
        detail.setOrderId(555555L);
        detail.setGoodsId(100L);
        detail.setGoodsNote("订单写入-足球");
        repository.save(detail);
    }


}

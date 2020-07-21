package com.wang.transaction.mul.orders.service;


import com.wang.transaction.mul.orders.entity.MulOrdersDetail;
import com.wang.transaction.mul.orders.repository.MulOrdersDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @description: 订单详情服务
 * @author: wei·man cui
 * @date: 2020/7/3 14:29
 */
@Service
public class MulOrdersDetailService {
    @Resource
    private MulOrdersDetailRepository repository;


    @Transactional
    public void saveDetail() {
        MulOrdersDetail detail = new MulOrdersDetail();
        detail.setOrderId(555555L);
        detail.setGoodsId(100L);
        detail.setGoodsNote("订单写入-足球");
        repository.save(detail);
    }


}

package com.wang.transaction.mul.orders.repository;

import com.wang.transaction.mul.orders.entity.MulOrdersDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @description: 订单详情
 * @author: wei·man cui
 * @date: 2020/7/3 10:35
 */
@Repository
public interface MulOrdersDetailRepository extends CrudRepository<MulOrdersDetail, Long> {
}

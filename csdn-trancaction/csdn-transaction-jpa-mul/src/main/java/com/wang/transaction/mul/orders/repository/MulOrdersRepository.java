package com.wang.transaction.mul.orders.repository;


import com.wang.transaction.mul.orders.entity.MulOrders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @description: Orders 订单
 * @author: weiman cui
 * @date: 2020/7/3 10:34
 */
@Repository
public interface MulOrdersRepository extends CrudRepository<MulOrders, Long> {
}

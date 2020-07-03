package com.wang.transaction.repository;

import com.wang.transaction.entity.Orders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @description: Orders 订单
 * @author: weiman cui
 * @date: 2020/7/3 10:34
 */
@Repository
public interface OrdersRepository extends CrudRepository<Orders, Long> {
}

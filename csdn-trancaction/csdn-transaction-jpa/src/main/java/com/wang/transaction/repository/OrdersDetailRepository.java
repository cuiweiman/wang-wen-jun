package com.wang.transaction.repository;

import com.wang.transaction.entity.OrdersDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @description: 订单详情
 * @author: weiman cui
 * @date: 2020/7/3 10:35
 */
@Repository
public interface OrdersDetailRepository extends CrudRepository<OrdersDetail, Long> {
}

package com.wang.transaction.mul.orders.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @description: 订单类
 * @author: wei·man cui
 * @date: 2020/7/3 10:18
 */
@Data
@Entity(name = "MulOrders")
@Table(name = "orders")
public class MulOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "order_status")
    private Integer orderStatus;

    @Column(name = "order_note")
    private String orderNote;
}

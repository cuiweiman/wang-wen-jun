package com.wang.transaction.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @description: 订单类
 * @author: wei·man cui
 * @date: 2020/7/3 10:18
 */
@Data
@Entity(name = "Orders")
@Table(name = "orders")
public class Orders {
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

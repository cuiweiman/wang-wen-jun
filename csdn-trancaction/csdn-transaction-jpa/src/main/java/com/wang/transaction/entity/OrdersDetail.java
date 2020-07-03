package com.wang.transaction.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @description: 订单详情类
 * @author: weiman cui
 * @date: 2020/7/3 10:19
 */
@Data
@Entity(name = "OrdersDetail")
@Table(name = "orders_detail")
public class OrdersDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "goods_id")
    private Long goodsId;

    @Column(name = "goods_note")
    private String goodsNote;
}

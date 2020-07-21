package com.wang.transaction.mul.stores.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @description: 库存详情类
 * @author: wei·man cui
 * @date: 2020/7/3 10:19
 */
@Data
@Entity(name = "MulStores")
@Table(name = "stores")
public class MulStores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "goods_id")
    private Long goodsId;

    @Column(name = "store")
    private Long store;
}

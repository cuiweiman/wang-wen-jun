package com.wang.es.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 商品
 * @date: 2020/11/26 23:21
 * @author: wei·man cui
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JdGoods {

    private String id;

    private String name;

    private String price;

    private String img;

    /**
     * 出版社-图书商品
     */
    private String publishingHouse;

}

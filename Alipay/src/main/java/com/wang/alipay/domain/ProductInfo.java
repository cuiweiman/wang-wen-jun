package com.wang.alipay.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 支付商品信息
 * @author: wei·man cui
 * @date: 2020/11/20 14:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo implements Serializable {

    private String out_trade_no;

    private String total_amount;

    private String subject;

    private String body;

    private String product_code;



}

package com.wang.crawlerdemo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * @description: 京东商品表(JdItem)表实体类
 * @author: wei·man cui
 * @date: 2020-11-02 10:06:19
 */
@Entity
@Data
@Table(name = "jd_item")
public class JdItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品集合id
     */
    private Long spu;

    /**
     * 商品最小品类单元id
     */
    private Long sku;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品价格
     */
    private Double price;

    /**
     * 商品图片
     */
    private String pic;

    /**
     * 商品详情地址
     */
    private String url;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 更新时间
     */
    private Date updated;
}
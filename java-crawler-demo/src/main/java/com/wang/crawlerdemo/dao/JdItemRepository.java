package com.wang.crawlerdemo.dao;

import com.wang.crawlerdemo.entity.JdItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description: 京东商品表(JdItem)表数据库访问层
 * @author: wei·man cui
 * @date: 2020-11-02 10:06:21
 */
public interface JdItemRepository extends JpaRepository<JdItem, Long> {

}
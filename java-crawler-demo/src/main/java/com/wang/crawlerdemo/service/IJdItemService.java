package com.wang.crawlerdemo.service;

import com.wang.crawlerdemo.entity.JdItem;

import java.util.List;

/**
 * @description: 京东商品表(JdItem)表服务接口
 * @author: wei·man cui
 * @date: 2020-11-02 10:06:22
 */
public interface IJdItemService {

    /**
     * 根据条件查询数据
     */
    public List<JdItem> findAll(JdItem item);

    /**
     * 保存数据
     */
    public void save(JdItem item);


}
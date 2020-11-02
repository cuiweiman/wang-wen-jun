package com.wang.crawlerdemo.service.impl;

import com.wang.crawlerdemo.dao.JdItemRepository;
import com.wang.crawlerdemo.entity.JdItem;
import com.wang.crawlerdemo.service.IJdItemService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 京东商品表(JdItem)表服务实现类
 * @author: wei·man cui
 * @date: 2020-11-02 10:06:23
 */
@Service
public class JdItemServiceImpl implements IJdItemService {
    @Resource
    private JdItemRepository jdItemRepository;


    @Override
    public List<JdItem> findAll(JdItem item) {
        Example example = Example.of(item);
        return this.jdItemRepository.findAll(example);
    }

    @Override
    public void save(JdItem item) {
        this.jdItemRepository.save(item);
    }
}
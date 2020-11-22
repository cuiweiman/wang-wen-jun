package com.wang.es.service.impl;

import com.wang.es.entity.SysUser;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

/**
 * <p>https://www.jianshu.com/p/5cfd2b929c92</p>
 *
 * @description: 使用基于 spring-data-elasticsearch 的 ElasticsearchRestTemplate 类操作 ES
 * @date: 2020/11/22 22:49
 * @author: wei·man cui
 */
@Service
@AllArgsConstructor
public class EsServiceImpl {

    private ElasticsearchRestTemplate esTemplate;

    public Boolean createIndex() {
        return esTemplate.indexOps(SysUser.class).create();
    }

    public Boolean createIndex(String indexName) {
        return esTemplate.indexOps(IndexCoordinates.of(indexName)).create();
    }


    public Boolean isIndexExist(String indexName) {

        return null;
    }

}

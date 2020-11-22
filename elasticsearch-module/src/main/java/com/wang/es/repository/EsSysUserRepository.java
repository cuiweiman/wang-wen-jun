package com.wang.es.repository;

import com.wang.es.entity.SysUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * <p>https://www.cnblogs.com/yfb918/p/10845326.html</p>
 *
 * @description: 使用 ElasticSearchRepository 操作 ES
 * @date: 2020/11/22 23:30
 * @author: wei·man cui
 */
public interface EsSysUserRepository extends ElasticsearchRepository<SysUser, Long> {
}

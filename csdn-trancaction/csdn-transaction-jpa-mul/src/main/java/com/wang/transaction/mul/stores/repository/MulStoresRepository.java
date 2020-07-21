package com.wang.transaction.mul.stores.repository;

import com.wang.transaction.mul.stores.entity.MulStores;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @description: 库存类 jpa接口
 * @date: 2020/7/5 11:44
 * @author: wei·man cui
 */
@Repository
public interface MulStoresRepository extends CrudRepository<MulStores, Long> {
}

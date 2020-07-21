package com.wang.transaction.mul.stores.service;

import com.wang.transaction.mul.stores.entity.MulStores;
import com.wang.transaction.mul.stores.repository.MulStoresRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @description: 库存类 服务接口
 * @date: 2020/7/5 11:45
 * @author: wei·man cui
 */
@Service
public class MulStoresService {

    @Resource
    private MulStoresRepository repository;

    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional(transactionManager = "storeTransactionManager")
    public void save() {
        MulStores mulStores = new MulStores();
        mulStores.setGoodsId(1L);
        mulStores.setStore(100L);
        repository.save(mulStores);
    }

}

package com.wang.boot.distributedlock;

import com.wang.boot.common.redis.RedisService;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description: RedisSon实现分布式锁：减库存场景
 * @author: wei·man cui
 * @date: 2021/3/11 17:16
 */
@RestController
@RequestMapping("distributedLock")
public class DistributedLockRedisSon {

    @Resource
    public RedisService redisService;

    @Resource
    public Redisson redisSon;

    @RequestMapping("/deduct")
    public String deductStock() {
        String lockKey = "product_ID";
        RLock redisSonLock = redisSon.getLock(lockKey);
        try {
            // 加锁
            redisSonLock.lock();
            int stock = Integer.parseInt(redisService.getCacheObject("stock"));
            if (stock > 0) {
                int realStock = stock - 1;
                redisService.setCacheObject("stock", realStock + "");
                System.out.println("扣库存成功,剩余库存：" + realStock);
            } else {
                System.out.println("扣库存失败,库存不足");
            }
        } finally {
            // 释放锁
            redisSonLock.unlock();
        }
        return "end";
    }

}

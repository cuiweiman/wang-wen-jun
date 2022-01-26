package com.wang.boot.bloomfilter;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @description: RedisSon 布隆过滤器的使用：数据存储在 Redis 中
 * @author: wei·man cui
 * @date: 2021/3/11 17:06
 */
public class RedisSonBloomFilter {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setPassword("***");
        //构造 RedisSon
        RedissonClient redisson = Redisson.create(config);

        RBloomFilter<String> bloomFilter = redisson.getBloomFilter("phoneList");
        //初始化布隆过滤器：预计元素为100000000L,误差率为 1%
        bloomFilter.tryInit(100000000L, 0.01);
        //将号码10086插入到布隆过滤器中
        bloomFilter.add("10086");

        //判断下面号码是否在布隆过滤器中
        System.out.println(bloomFilter.contains("123456"));
        System.out.println(bloomFilter.contains("10086"));
    }
}

package com.wang.redis.cache;

import com.wang.redis.config.RedisBasicComponent;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.ObjectListener;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Redis String 缓存 API
 * @Author: cuiweiman
 * @Since: 2021/6/10 下午7:39
 */
@Component
public class BucketOperation<T> {

    @Resource
    private RedisBasicComponent redisContainer;

    /**
     * 简单获取
     *
     * @param key key
     * @return 值，字符串或整型
     */
    public T getVal(String key) {
        RBucket<T> rBucket = redisContainer.getRBucket(key);
        return rBucket.get();
    }

    /**
     * 设置缓存
     *
     * @param key key
     * @param val value
     */
    public void setVal(String key, T val) {
        setVal(key, val, null);
    }

    /**
     * 设置缓存
     *
     * @param key        key
     * @param val        value
     * @param timeToLive expire time，单位：秒
     */
    public void setVal(String key, T val, Long timeToLive) {
        if (StringUtils.isBlank(key) || Objects.isNull(val)) {
            throw new RuntimeException("key 或 value 不能为空");
        }
        RBucket<T> rBucket = redisContainer.getRBucket(key);
        if (Objects.isNull(timeToLive)) {
            rBucket.set(val);
        } else {
            rBucket.set(val, timeToLive, TimeUnit.SECONDS);
        }
    }

    /**
     * 获取 旧值 并更新为新值
     *
     * @param key    key
     * @param newVal 新值
     * @return 旧值
     */
    public T getAndSet(String key, T newVal) {
        return this.getAndSet(key, newVal, null);
    }

    public T getAndSet(String key, T newVal, Long timeToLive) {
        if (StringUtils.isBlank(key) || Objects.isNull(newVal)) {
            throw new RuntimeException("key 或 value 不能为空");
        }
        RBucket<T> rBucket = redisContainer.getRBucket(key);
        if (Objects.isNull(timeToLive)) {
            return rBucket.getAndSet(newVal);
        }
        return rBucket.getAndSet(newVal, timeToLive, TimeUnit.SECONDS);
    }

    /**
     * 设置 key-value，若 key 已存在，则保留 key 过期时间
     *
     * @param key key
     * @param val value
     */
    public void setAndKeepTTL(String key, T val) {
        if (StringUtils.isBlank(key) || Objects.isNull(val)) {
            throw new RuntimeException("key 或 value 不能为空");
        }
        RBucket<T> rBucket = redisContainer.getRBucket(key);
        rBucket.setAndKeepTTL(val);
    }

    /**
     * 尝试 存储 key-value
     * 若 key 已存在则 存储失败
     *
     * @param key    key
     * @param newVal value
     * @return 结果：true-存储成功；false-存储失败，已存在同名key
     */
    public boolean trySet(String key, T newVal) {
        return this.trySet(key, newVal, null);
    }

    public boolean trySet(String key, T newVal, Long timeToLive) {
        if (StringUtils.isBlank(key) || Objects.isNull(newVal)) {
            throw new RuntimeException("key 或 value 不能为空");
        }
        RBucket<T> rBucket = redisContainer.getRBucket(key);
        if (Objects.isNull(timeToLive)) {
            return rBucket.trySet(newVal);
        }
        return rBucket.trySet(newVal, timeToLive, TimeUnit.SECONDS);
    }


    /**
     * 更新 value， 若 key 不存在则更新操作失败
     *
     * @param key key
     * @param val value
     */
    public void setIfExists(String key, T val) {
        this.setIfExists(key, val, null);
    }

    public void setIfExists(String key, T val, Long timeToLive) {
        if (StringUtils.isBlank(key) || Objects.isNull(val)) {
            throw new RuntimeException("key 或 value 不能为空");
        }
        RBucket<T> rBucket = redisContainer.getRBucket(key);
        if (Objects.isNull(timeToLive)) {
            rBucket.setIfExists(val);
        } else {
            rBucket.setIfExists(val, timeToLive, TimeUnit.SECONDS);
        }
    }

    /**
     * 设置 ttl ，倒计时删除
     *
     * @param key        key
     * @param timeToLive 存活时间
     * @return 结果
     */
    public boolean expire(String key, Long timeToLive) {
        RBucket<T> rBucket = redisContainer.getRBucket(key);
        return rBucket.expire(timeToLive, TimeUnit.SECONDS);
    }

    /**
     * 设置 删除 时间戳，毫秒级时间戳
     *
     * @param key       key
     * @param timestamp 时间戳
     * @return 到点删除
     */
    public boolean expireAt(String key, Long timestamp) {
        RBucket<T> rBucket = redisContainer.getRBucket(key);
        return rBucket.expireAt(timestamp);
    }

    /**
     * 删除键
     *
     * @param key key
     * @return 结果
     */
    public boolean del(String key) {
        RBucket<T> rBucket = redisContainer.getRBucket(key);
        return rBucket.delete();
    }

    /**
     * 获取值后立刻删除
     *
     * @param key key
     * @return value
     */
    public T getAndDel(String key) {
        RBucket<T> rBucket = redisContainer.getRBucket(key);
        return rBucket.getAndDelete();
    }

    /**
     * CAS 更新值
     *
     * @param key         key
     * @param exceptedVal 期望值
     * @param newVal      新值
     * @return 结果
     */
    public boolean compareAndSet(String key, T exceptedVal, T newVal) {
        RBucket<T> rBucket = redisContainer.getRBucket(key);
        return rBucket.compareAndSet(exceptedVal, newVal);
    }

    /**
     * key 是否存在
     *
     * @param key key
     * @return 结果
     */
    public boolean isExists(String key) {
        RBucket<T> rBucket = redisContainer.getRBucket(key);
        return rBucket.isExists();
    }

    /**
     * 事件监听器，针对 过期事件 和 删除事件
     *
     * @param key      key
     * @param listener 事件监听器
     * @return 监听器 ID
     */
    public int addListener(String key, ObjectListener listener) {
        RBucket<T> rBucket = redisContainer.getRBucket(key);
        return rBucket.addListener(listener);
    }

    /**
     * 移除 事件监听器
     *
     * @param key        key
     * @param listenerId 监听器ID
     */
    public void removeListener(String key, int listenerId) {
        RBucket<T> rBucket = redisContainer.getRBucket(key);
        rBucket.removeListener(listenerId);
    }
}

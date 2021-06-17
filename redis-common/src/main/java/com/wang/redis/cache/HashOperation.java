package com.wang.redis.cache;

import com.wang.redis.config.RedisBasicComponent;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMap;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @Description: 分布式 哈希
 * @Author: cuiweiman
 * @Since: 2021/6/11 上午11:17
 */
@Component
public class HashOperation<K, V> {

    @Resource
    private RedisBasicComponent redisContainer;

    /**
     * 写入数据
     * 拥有写锁后，将 field-value 写入缓存
     *
     * @param key   key
     * @param field field
     * @param value value
     * @return 结果
     */
    public boolean put(String key, K field, V value) {
        if (StringUtils.isBlank(key) || Objects.isNull(field) || Objects.isNull(value)) {
            throw new RuntimeException("关键参数不能为空");
        }
        RMap<K, V> rMap = redisContainer.getRMap(key);
        ReadWriteLock readWriteLock = rMap.getReadWriteLock(field);
        boolean locked = false;
        try {
            locked = readWriteLock.writeLock().tryLock(5, TimeUnit.SECONDS);
            if (locked) {
                rMap.put(field, value);
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Redis Hash 更新失败：" + e.getMessage());
        } finally {
            if (locked) {
                readWriteLock.writeLock().unlock();
            }
        }
        return false;
    }

    /**
     * 缓存
     * 若之前 field 不存在，则成功；否则失败。
     *
     * @param key   key
     * @param field field
     * @param value value
     * @return 结果
     */
    public boolean putIfAbsent(String key, K field, V value) {
        if (StringUtils.isBlank(key) || Objects.isNull(field) || Objects.isNull(value)) {
            throw new RuntimeException("关键参数不能为空");
        }
        try {
            RMap<K, V> rMap = redisContainer.getRMap(key);
            V oldVal = rMap.putIfAbsent(field, value);
            if (!Objects.isNull(oldVal)) {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Redis Hash 缓存失败：" + e.getMessage());
        }
        return false;
    }

    /**
     * 读取缓存
     *
     * @param key   key
     * @param field field
     * @return 值
     */
    public V get(String key, K field) {
        if (StringUtils.isBlank(key) || Objects.isNull(field)) {
            throw new RuntimeException("关键参数不能为空");
        }
        RMap<K, V> rMap = redisContainer.getRMap(key);
        return rMap.get(field);
    }

    /**
     * 返回 key 中所有的 field
     *
     * @param key key
     * @return field 集合
     */
    public Set<K> keySet(String key) {
        RMap<K, V> rMap = redisContainer.getRMap(key);
        return rMap.keySet();
    }

    /**
     * 返回 key 中所有的 field
     *
     * @param key     key
     * @param pattern 筛选
     * @param count   获取的个数
     * @return field 集合
     */
    public Set<K> keySet(String key, String pattern, int count) {
        RMap<K, V> rMap = redisContainer.getRMap(key);
        return rMap.keySet(pattern, count);
    }

    /**
     * 移除缓存属性
     *
     * @param key   key
     * @param field 属性
     * @return 结果
     */
    public boolean remove(String key, K field) {
        if (Objects.isNull(field)) {
            return false;
        }
        RMap<K, V> rMap = redisContainer.getRMap(key);
        ReadWriteLock readWriteLock = rMap.getReadWriteLock(field);
        boolean locked = false;
        try {
            locked = readWriteLock.writeLock().tryLock(5, TimeUnit.SECONDS);
            if (locked) {
                rMap.remove(field);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (locked) {
                readWriteLock.writeLock().unlock();
            }
        }
        return false;
    }

    /**
     * 获取整个 map 对象
     *
     * @param key key
     * @return map 对象
     */
    public Map<K, V> all(String key) {
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("关键参数不能为空");
        }
        RMap<K, V> rMap = redisContainer.getRMap(key);
        return rMap.readAllMap();
    }

    /**
     * 清空 map 对象的 field 内容
     *
     * @param key key
     */
    public void clear(String key) {
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("关键参数不能为空");
        }
        RMap<K, V> rMap = redisContainer.getRMap(key);
        rMap.clear();
    }


    /**
     * 删除 map 对象
     *
     * @param key key
     */
    public void del(String key) {
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("关键参数不能为空");
        }
        RMap<K, V> rMap = redisContainer.getRMap(key);
        rMap.delete();
    }

    /**
     * 设置过期时间 倒计时
     *
     * @param key  key
     * @param time 过期倒计时
     * @param unit 单位
     * @return 结果
     */
    public boolean expire(String key, Long time, TimeUnit unit) {
        RMap<K, V> rMap = redisContainer.getRMap(key);
        return rMap.expire(time, unit);
    }

    /**
     * 设置过期时间 指定时间过期
     *
     * @param key       key
     * @param timestamp 毫秒级 时间戳
     * @return 结果
     */
    public boolean expireAt(String key, Long timestamp) {
        RMap<K, V> rMap = redisContainer.getRMap(key);
        return rMap.expireAt(timestamp);
    }

}











package com.wang.redis.cache;

import com.wang.redis.config.RedisBasicComponent;
import org.redisson.api.RSet;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: set 集合 api
 * @Author: cuiweiman
 * @Since: 2021/6/11 上午11:17
 */
@Component
public class SetOperation<T> {

    @Resource
    private RedisBasicComponent redisContainer;

    /**
     * 添加元素
     *
     * @param key   key
     * @param value value
     * @return 结果
     */
    public boolean add(String key, T value) {
        RSet<Object> rSet = redisContainer.getRSet(key);
        return rSet.add(value);
    }

    /**
     * 添加元素集合
     *
     * @param key       key
     * @param valueList value集
     * @return 结果
     */
    public boolean addAll(String key, Set<T> valueList) {
        RSet<Object> rSet = redisContainer.getRSet(key);
        return rSet.addAll(valueList);
    }

    /**
     * 尝试 添加 元素集合，集合中的元素一个都不存在时，才能添加成功
     *
     * @param key    key
     * @param values value
     * @return 结果
     */
    public boolean tryAdd(String key, T... values) {
        RSet<Object> rSet = redisContainer.getRSet(key);
        return rSet.tryAdd(values);
    }

    /**
     * 删除指定 元素
     *
     * @param key   key
     * @param value value
     * @return 结果
     */
    public boolean remove(String key, T value) {
        RSet<Object> rSet = redisContainer.getRSet(key);
        return rSet.remove(value);
    }

    /**
     * 随机 弹出元素，元素仍存在于集合中
     *
     * @param key key
     * @return 元素
     */
    public T random(String key) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.random();
    }

    /**
     * 随机 弹出 指定数量 的元素，元素仍存在于集合中
     *
     * @param key   key
     * @param count 指定数量
     * @return 元素
     */
    public Set<T> random(String key, int count) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.random(count);
    }

    /**
     * 随机 弹出元素，集合中元素会被删除
     *
     * @param key key
     * @return 元素
     */
    public T removeRandom(String key) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.removeRandom();
    }

    /**
     * 随机 弹出 指定数量 的元素，集合中元素会被删除
     *
     * @param key   key
     * @param count 指定数量
     * @return 元素
     */
    public Set<T> removeRandom(String key, int count) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.removeRandom(count);
    }

    /**
     * 删除指定 key 的集合
     *
     * @param key key
     * @return 删除结果
     */
    public boolean del(String key) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.delete();
    }

    /**
     * 读取所有元素
     *
     * @param key key
     * @return 所有元素
     */
    public Set<T> readAll(String key) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.readAll();
    }

    /**
     * 遍历 指定个数 的元素
     *
     * @param key   key
     * @param count 指定个数
     * @return 结果集
     */
    public Iterator<T> iterator(String key, int count) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.iterator(count);
    }

    /**
     * 遍历 指定个数 的元素
     *
     * @param key     key
     * @param pattern 匹配字符，用于筛选
     * @param count   指定个数
     * @return 结果集
     */
    public Iterator<T> iterator(String key, String pattern, int count) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.iterator(pattern, count);
    }

    /**
     * 将 member 从 key 移入到 destinationKey
     *
     * @param key            当前key】
     * @param destinationKey 目标 key
     * @param member         当前 key 元素
     * @return 结果
     */
    public boolean move(String key, String destinationKey, T member) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.move(destinationKey, member);
    }

    /**
     * 并集运算，结果存入 key 集合中。
     * <p>
     * otherKeys 集合之间 做并集运算，并将结果写入当前 key的 集合set
     *
     * @param key       当前key
     * @param otherKeys 其他key
     * @return 并集运算后的 当前 set 集合长度
     */
    public int union(String key, String... otherKeys) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.union(otherKeys);
    }

    /**
     * 并集运算，不存储结果
     * <p>
     * 当前 set 集合 与 otherKeys其他 set 集合 做并集运算, 只是返回运算结果，不做存储
     *
     * @param key       当前key
     * @param otherKeys 其他key
     * @return 并集运算 结果
     */
    public Set<T> readUnion(String key, String... otherKeys) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.readUnion(otherKeys);
    }


    /**
     * 差集运算，结果存入当前 key 集合。
     * <p>
     * otherKeys 集合 之间 做差集运算，并将结果写入当前 key 集合set
     *
     * @param key       当前key
     * @param otherKeys 其他key
     * @return 差集运算后的 当前 set 集合长度
     */
    public int diff(String key, String... otherKeys) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.diff(otherKeys);
    }

    /**
     * 差集运算，不存储结果。
     * <p>
     * 当前 set 集合 依次 与 其他 set 集合 做差集运算, 只是返回运算结果，不做存储
     *
     * @param key       当前key
     * @param otherKeys 其他key
     * @return 差集运算结果
     */
    public Set<T> readDiff(String key, String... otherKeys) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.readDiff(otherKeys);
    }


    /**
     * 交集运算，结果存入当前 key 集合
     * <p>
     * otherKeys 集合 做交集运算，并将结果写入当前 key 集合set
     *
     * @param key       当前key
     * @param otherKeys 其他key
     * @return 交集运算后的 当前 set 集合长度
     */
    public int intersection(String key, String... otherKeys) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.intersection(otherKeys);
    }

    /**
     * 交集运算，不存储结果。
     * <p>
     * 当前 set 集合 与 其他 set 集合 做交集运算, 只是返回运算结果，不做存储
     *
     * @param key       当前key
     * @param otherKeys 其他key
     * @return 交集运算结果
     */
    public Set<T> readIntersection(String key, String... otherKeys) {
        RSet<T> rSet = redisContainer.getRSet(key);
        return rSet.readIntersection(otherKeys);
    }


}

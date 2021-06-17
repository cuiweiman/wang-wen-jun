package com.wang.redis.cache;

import com.wang.redis.config.RedisBasicComponent;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RList;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 列表 api
 * @Author: cuiweiman
 * @Since: 2021/6/11 上午11:16
 */
@Component
public class ListOperation<T> {

    @Resource
    private RedisBasicComponent redisContainer;

    /**
     * 快速添加
     *
     * @param key   key
     * @param index 下标
     * @param value value
     */
    public void fastAdd(String key, Integer index, T value) {
        if (StringUtils.isBlank(key) || Objects.isNull(value)) {
            throw new RuntimeException("关键参数不能为空");
        }
        RList<Object> rList = redisContainer.getRList(key);
        rList.fastSet(index, value);
    }

    /**
     * 添加指定字段
     *
     * @param key   key
     * @param value value
     * @return 结果
     */
    public boolean add(String key, T value) {
        if (StringUtils.isBlank(key) || Objects.isNull(value)) {
            throw new RuntimeException("关键参数不能为空");
        }
        RList<Object> rList = redisContainer.getRList(key);
        return rList.add(value);
    }

    /**
     * 向 list 中 element 元素之前，加入 value
     *
     * @param key     key
     * @param element element
     * @param value   value
     * @return new list size
     */
    public int addBefore(String key, T element, T value) {
        if (StringUtils.isBlank(key) || Objects.isNull(value)) {
            throw new RuntimeException("关键参数不能为空");
        }
        RList<Object> rList = redisContainer.getRList(key);
        return rList.addBefore(element, value);
    }


    /**
     * 向 list 中 element 元素之后，加入 value
     *
     * @param key     key
     * @param element element
     * @param value   value
     * @return new list size
     */
    public int addAfter(String key, T element, T value) {
        if (StringUtils.isBlank(key) || Objects.isNull(value)) {
            throw new RuntimeException("关键参数不能为空");
        }
        RList<Object> rList = redisContainer.getRList(key);
        return rList.addAfter(element, value);
    }

    /**
     * 添加全部
     *
     * @param key    key
     * @param values value 集合
     */
    public boolean addAll(String key, List<T> values) {
        if (StringUtils.isBlank(key) || CollectionUtils.isEmpty(values)) {
            throw new RuntimeException("关键参数不能为空");
        }
        RList<Object> rList = redisContainer.getRList(key);
        return rList.addAll(values);
    }

    /**
     * 获取指定下标的 value
     *
     * @param key   key
     * @param index 下标
     * @return value
     */
    public T get(String key, int index) {
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("关键参数不能为空");
        }
        RList<T> rList = redisContainer.getRList(key);
        return rList.get(index);
    }

    /**
     * 获取 指定下标集合的 值
     *
     * @param key       key
     * @param indexList 下标集合
     * @return 结果集
     */
    public List<T> get(String key, int... indexList) {
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("关键参数不能为空");
        }
        RList<T> rList = redisContainer.getRList(key);
        return rList.get(indexList);
    }

    /**
     * 删除指定下标的元素
     *
     * @param key   key
     * @param index 下标
     * @return 被删除值
     */
    public T remove(String key, int index) {
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("关键参数不能为空");
        }
        RList<T> rList = redisContainer.getRList(key);
        return rList.remove(index);
    }

    /**
     * 返回指定下标的 范围值
     * [from,to]
     *
     * @param key  key
     * @param from 始
     * @param to   终
     * @return 结果集
     */
    public List<T> range(String key, int from, int to) {
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("关键参数不能为空");
        }
        if (from < 0 || to < 0 || from > to) {
            // 必须明确 from～to，避免数据集过大造成 慢查询 堵塞线程
            throw new RuntimeException("获取范围不合法");
        }
        RList<T> rList = redisContainer.getRList(key);
        return rList.range(from, to);
    }

    /**
     * 保留 from-to 范围下标的 元素，其他的全部删除
     * [from,to]
     *
     * @param key  key
     * @param from from index
     * @param to   to index
     */
    public void trim(String key, int from, int to) {
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("关键参数不能为空");
        }
        if (from < 0 || to < 0 || from > to) {
            // 必须明确 from～to
            throw new RuntimeException("截取范围不合法");
        }
        RList<T> rList = redisContainer.getRList(key);
        rList.trim(from, to);
    }
}

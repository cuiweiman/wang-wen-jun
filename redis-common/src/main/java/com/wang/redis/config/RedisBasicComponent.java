package com.wang.redis.config;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RBitSet;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RBoundedBlockingQueue;
import org.redisson.api.RBucket;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RDeque;
import org.redisson.api.RGeo;
import org.redisson.api.RHyperLogLog;
import org.redisson.api.RIdGenerator;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RQueue;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RSet;
import org.redisson.api.RSortedSet;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: Redisson 桶容器映射、锁相关、地理空间信息等API
 * @Author: cuiweiman
 * @Since: 2021/6/10 下午2:31
 */
@Component
public class RedisBasicComponent {

    @Resource
    private RedissonClient client;

    /**
     * bucket 桶 映射 String 类型，获取 RBucket
     *
     * @param key key
     * @return String bucket 桶
     */
    public <T> RBucket<T> getRBucket(String key) {
        return client.getBucket(key);
    }

    /**
     * RList 映射 list 结构
     *
     * @param key key
     * @param <T> element's type
     * @return RList
     */
    public <T> RList<T> getRList(String key) {
        return client.getList(key);
    }

    /**
     * RMap 映射 Map 哈希结构
     *
     * @param key key
     * @param <K> element's key type
     * @param <V> element's value type
     * @return 哈希结构
     */
    public <K, V> RMap<K, V> getRMap(String key) {
        return client.getMap(key);
    }

    /**
     * RSet 映射 Set 集合
     *
     * @param key key
     * @param <T> element's type
     * @return RSet -> set
     */
    public <T> RSet<T> getRSet(String key) {
        return client.getSet(key);
    }

    /**
     * RSortedSet 有序集合 ZSet
     *
     * @param key key
     * @param <T> element's type
     * @return sorted set->ZSet
     */
    public <T> RSortedSet<T> getRSortedSet(String key) {
        return client.getSortedSet(key);
    }

    /**
     * 获取 队列
     *
     * @param key key
     * @param <T> element's type
     * @return 队列
     */
    public <T> RQueue<T> getRQueue(String key) {
        return client.getQueue(key);
    }

    /**
     * 双端 队列
     *
     * @param key key
     * @param <T> element's type
     * @return 双端 队列
     */
    public <T> RDeque<T> getRDeque(String key) {
        return client.getDeque(key);
    }

    /**
     * 获取 阻塞队列
     *
     * @param key key
     * @param <T> element's type
     * @return 队列
     */
    public <T> RBlockingQueue<T> getRBlockingQueue(String key) {
        return client.getBlockingQueue(key);
    }

    /**
     * 获取 有界的 阻塞队列
     * <p>
     * 入队：队列满则阻塞，直到入队线程被中断或队列有位置；
     * 出对：队列空则阻塞，直到出对线程被中断或队列有元素
     *
     * @param key key
     * @param <T> element's type
     * @return 有界的阻塞队列
     */
    public <T> RBoundedBlockingQueue<T> getRBoundedBlockingQueue(String key) {
        return client.getBoundedBlockingQueue(key);
    }

    /**
     * 获取 Long 类型 原子数
     *
     * @param key key
     * @return 原子数
     */
    public RAtomicLong getRAtomicLong(String key) {
        return client.getAtomicLong(key);
    }

    /**
     * 获取锁
     *
     * @param key key
     * @return 锁
     */
    public RLock getRLock(String key) {
        return client.getLock(key);
    }

    /**
     * 获取 读写锁
     *
     * @param key key
     * @return 读写锁
     */
    public RReadWriteLock getRReadWriteLock(String key) {
        return client.getReadWriteLock(key);
    }

    /**
     * 获取 联锁
     *
     * @param rLock 锁数组
     * @return MultiLock
     */
    public RLock getMultiLock(RLock... rLock) {
        return client.getMultiLock(rLock);
    }

    /**
     * 获取信号量
     *
     * @param key key
     * @return 信号量
     */
    public RSemaphore getSemaphore(String key) {
        return client.getSemaphore(key);
    }

    /**
     * 获取 RBitSet 数据分片
     *
     * @param key key
     * @return RBitSet
     */
    public RBitSet getBitSet(String key) {
        return client.getBitSet(key);
    }

    /**
     * 获取 布隆过滤器
     *
     * @param key key
     * @param <V> 类型
     * @return 布隆过滤器
     */
    public <V> RBloomFilter<V> getBloomFilter(String key) {
        return client.getBloomFilter(key);
    }

    /**
     * 获取 分布式线程计数锁
     *
     * @param key key
     * @return 分布式计数锁
     */
    public RCountDownLatch getRCountDownLatch(String key) {
        return client.getCountDownLatch(key);
    }

    /**
     * 获取 消息的 topic
     *
     * @param key key
     * @return topic
     */
    public RTopic getRTopic(String key) {
        return client.getTopic(key);
    }

    /**
     * 地理空间信息
     *
     * @param key key
     * @param <V> V
     * @return 地理空间信息
     */
    public <V> RGeo<V> getGeo(String key) {
        return client.getGeo(key);
    }

    public <V> RHyperLogLog<V> getHyperLogLog(String key) {
        return client.getHyperLogLog(key);
    }


    /**
     * ID 生成器
     *
     * @param key key
     * @return ID生成器
     */
    public RIdGenerator getIdGenerator(String key) {
        return client.getIdGenerator(key);
    }

    /*
     * API 是真的多，……， 暂时先到这里吧
     */

    /**
     * 关闭 Redisson 客户端
     * 项目运行期间，为什么要关闭呢？
     *
     * @param redissonClient 客户端
     */
    public void closeRedisson(RedissonClient redissonClient) {
        if (!redissonClient.isShuttingDown()) {
            redissonClient.shutdown();
        }
    }

}







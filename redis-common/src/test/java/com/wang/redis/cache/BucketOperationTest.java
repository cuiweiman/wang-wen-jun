package com.wang.redis.cache;

import com.wang.redis.RedisCommonAppTest;
import com.wang.redis.config.RedisBasicComponent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.ObjectListener;
import org.redisson.api.RCountDownLatch;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Bucket 映射 String 测试
 * @Author: cuiweiman
 * @Since: 2021/6/10 下午2:03
 */
@Slf4j
public class BucketOperationTest extends RedisCommonAppTest {

    @Resource
    private BucketOperation<String> bucketOperation;

    @Resource
    private RedisBasicComponent redisBasicComponent;

    @Test
    public void testBasic() {
        bucketOperation.setVal("cwm", "Fighting!");
        bucketOperation.setVal("syy", "100", 100L);
        log.info("cwm: {}", bucketOperation.getVal("cwm"));
        log.info("syy: {}", bucketOperation.getAndSet("syy", "22222"));

        log.info("compareAndSet syy: {}", bucketOperation.compareAndSet("syy", "22222", "100"));
        log.info("syy: {}", bucketOperation.getAndDel("syy"));
        log.info("del cwm: {}", bucketOperation.del("cwm"));
    }

    @Test
    public void testIf() {
        bucketOperation.setIfExists("testBucketName", "setIfExists");
        if (bucketOperation.isExists("syy")) {
            bucketOperation.setIfExists("syy", "syy-setIfExists", 30L);
        } else {
            bucketOperation.trySet("syy", "100", 90L);
        }
        log.info("testBucketName: {}", bucketOperation.getVal("testBucketName"));
        log.info("syy: {}", bucketOperation.getVal("syy"));
    }


    @Test
    public void testExpire() {
        bucketOperation.setVal("expire", "expire");
        bucketOperation.setVal("syy", "syy-setIfExists");
        bucketOperation.expire("expire", 20L);
        bucketOperation.expireAt("syy", 1623380399000L);

        log.info("expire: {}", bucketOperation.getVal("expire"));
        log.info("syy: {}", bucketOperation.getVal("syy"));
    }

    @Test
    public void testListener() throws InterruptedException {
        RCountDownLatch listener = redisBasicComponent.getRCountDownLatch("listener");
        listener.trySetCount(2);

        ObjectListener syyExpiredListener = (ExpiredObjectListener) name -> {
            log.info("syy 过期：{}", name);
            listener.countDown();
        };
        bucketOperation.addListener("syy", syyExpiredListener);

        ObjectListener wrongDelListener = (DeletedObjectListener) name -> {
            log.info("wrong 删除：{}", name);
            listener.countDown();
        };
        bucketOperation.addListener("wrong", wrongDelListener);

        bucketOperation.setVal("syy", "100", 15L);
        bucketOperation.setVal("wrong", "nothing");

        TimeUnit.SECONDS.sleep(3L);
        bucketOperation.del("wrong");

        listener.await(20L, TimeUnit.SECONDS);
        log.info("program finished!");

    }

}

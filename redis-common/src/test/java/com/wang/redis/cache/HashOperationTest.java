package com.wang.redis.cache;

import com.wang.redis.RedisCommonAppTest;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Map;


public class HashOperationTest extends RedisCommonAppTest {

    @Resource
    private HashOperation<String, Object> hashOperation;

    @Test
    public void test() {
        String key = "user:1001";
        String field = "name";
        String value = "syy";
        String field1 = "age";
        Integer value1 = 18;
        hashOperation.put(key, field, value);
        hashOperation.put(key, field1, value1);

        System.out.println(hashOperation.get(key, field1));

        System.out.println(hashOperation.putIfAbsent(key, field, value));

        System.out.println(hashOperation.remove(key, field1));

        Map<String, Object> all = hashOperation.all(key);
        all.forEach((k, v) -> System.out.println(k + " ====== " + v));

        hashOperation.clear(key);

    }

}
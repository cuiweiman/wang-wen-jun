package com.wang.guava.concurrent;

/**
 * @description: 令牌桶 测试
 * @author: wei·man cui
 * @date: 2020/8/17 17:02
 */
public class TokenBuckeTest {
    public static void main(String[] args) {
        final TokenBucket tokenBucket = new TokenBucket();
        for (int i = 0; i < 250; i++) {
            new Thread(tokenBucket::buy).start();
        }
    }
}

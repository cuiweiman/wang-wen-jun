package com.wang.boot.idempotent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description: token 创建和验证方法
 * @author: wei·man cui
 * @date: 2021/2/2 10:47
 */
@Slf4j
@Service
@AllArgsConstructor
public class TokenUtilService {

    private StringRedisTemplate redisTemplate;

    private static final String IDEMPOTENT_TOKEN_PREFIX = "idempotent_token:";

    /**
     * 创建 Token 令牌
     *
     * @param value 内容
     * @return 令牌
     */
    public String generateToken(String value) {
        String token = UUID.randomUUID().toString();
        String key = IDEMPOTENT_TOKEN_PREFIX + token;
        // 过期时间 5 分钟
        redisTemplate.opsForValue().set(key, value, 5, TimeUnit.MINUTES);
        return token;
    }

    public boolean validToken(String token, String value) {
        // 设置 Lua 脚本，其中 KEYS[1] 是 key，KEYS[2] 是 value
        String script = "if redis.call('get',KEYS[1])==KEYS[2] then return redis.call('del',KEYS[1]) else return 0 end";
        final RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        String key = IDEMPOTENT_TOKEN_PREFIX + token;
        Long result = redisTemplate.execute(redisScript, Arrays.asList(key, value));
        if (!ObjectUtils.isEmpty(result) && result != 0L) {
            log.info("验证 token={},key={},value={} 成功", token, key, value);
            return true;
        }
        log.info("验证 token={},key={},value={} 失败", token, key, value);
        return false;
    }

}

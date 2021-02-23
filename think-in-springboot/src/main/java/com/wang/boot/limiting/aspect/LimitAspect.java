package com.wang.boot.limiting.aspect;

import com.google.common.collect.ImmutableList;
import com.wang.boot.limiting.annotation.Limit;
import com.wang.boot.limiting.enums.GlobalLimitType;
import com.wang.boot.limiting.utils.IpUtils;
import com.wang.boot.limiting.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @description: 接口限流 切面
 * @author: wei·man cui
 * @date: 2021/2/22 17:46
 */
@Slf4j
@Aspect
@Component
public class LimitAspect {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(com.wang.boot.limiting.annotation.Limit)")
    public void limit() {
    }

    @Around("limit()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        Method method = resolveMethod(point);
        Limit annotation = method.getAnnotation(Limit.class);
        GlobalLimitType globalLimitType = annotation.limitType();

        String key;
        HttpServletRequest request = ServletUtils.getRequest();
        String ip = IpUtils.getIpAddr(request);
        switch (globalLimitType) {
            case IP:
                key = ip;
                break;
            case CUSTOMER:
                key = annotation.key();
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }

        int count = annotation.count();
        int period = annotation.period();
        String name = annotation.name();

        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(annotation.prefix() + "_", key, ip));
        String luaScript = this.buildLuaScript();
        DefaultRedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
        Number number = redisTemplate.execute(redisScript, keys, count, period);
        log.info("IP:{} 第 {} 次访问key为 {}，描述为 [{}] 的接口", ip, count, keys, name);
        if (Objects.nonNull(number) && number.intValue() <= count) {
            return point.proceed();
        } else {
            return "请勿频繁操作";
        }
    }

    /**
     * 限流脚本
     * 调用的时候不超过阈值，则直接返回并执行计算器自加。
     *
     * @return lua脚本
     */
    private String buildLuaScript() {
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }


    private Method resolveMethod(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?> targetClass = point.getTarget().getClass();
        Method method = getDeclaredMethod(targetClass, signature.getName(), signature.getMethod().getParameterTypes());

        if (Objects.isNull(method)) {
            throw new IllegalStateException("无法解析目标方法：" + signature.getMethod().getName());
        }

        return method;
    }


    private Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (Objects.nonNull(superClass)) {
                return getDeclaredMethod(superClass, name, parameterTypes);
            }
        }
        return null;
    }

}

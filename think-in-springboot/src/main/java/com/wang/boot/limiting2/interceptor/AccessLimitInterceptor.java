package com.wang.boot.limiting2.interceptor;

import com.alibaba.fastjson.JSON;
import com.wang.boot.limiting.utils.IpUtils;
import com.wang.boot.limiting2.annotation.AccessLimit;
import com.wang.boot.limiting2.service.RedisService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @description: 接口限流防刷 拦截器
 * @author: wei·man cui
 * @date: 2021/2/23 10:08
 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Resource
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断请求是否属于方法的请求
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            //获取方法中的注解,看是否有该注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            long seconds = accessLimit.period();
            int maxCount = accessLimit.count();

            // 从 redis 中获取用户访问的次数
            String ip = IpUtils.getIpAddr(request);
            String key = accessLimit.prefix().concat(accessLimit.key()).concat(ip);

            Integer count = redisService.getCacheObject(key);
            if (count == null) {
                //第一次访问
                redisService.setCacheObject(key, 1, seconds, TimeUnit.SECONDS);
            } else if (count < maxCount) {
                //加 1
                redisService.increment(key);
            } else {
                //超出访问次数
                render(response, "访问频繁");
                return false;
            }
        }
        return true;
    }


    private void render(HttpServletResponse response, String msg) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(msg);
        out.write(str.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }


}

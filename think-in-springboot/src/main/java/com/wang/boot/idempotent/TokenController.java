package com.wang.boot.idempotent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 测试用的 Controller 类
 * @author: wei·man cui
 * @date: 2021/2/2 10:58
 * @see com.wang.boot.idempotent.IdempotenceTest (test)
 */
@Slf4j
@RestController
@AllArgsConstructor
public class TokenController {

    private TokenUtilService tokenUtilService;

    @GetMapping("/token")
    public String getToken() {
        String userInfo = "cui";
        return tokenUtilService.generateToken(userInfo);
    }

    @PostMapping("/test")
    public String test(@RequestHeader("token") String token) {
        String userInfo = "cui";
        final boolean result = tokenUtilService.validToken(token, userInfo);
        return result ? "正常调用" : "重复调用";
    }

}

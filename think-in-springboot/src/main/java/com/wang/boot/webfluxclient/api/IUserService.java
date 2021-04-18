package com.wang.boot.webfluxclient.api;

import com.wang.boot.webflux.domain.User;
import com.wang.boot.webflux.routes.UserRouterDispatcherServlet;
import com.wang.boot.webfluxclient.annotation.ApiServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @description: 这里调用的是 {@link UserRouterDispatcherServlet} 中 设定的接口路由。类似于 Feign 的服务调用
 * @date: 2021/4/18 11:45
 * @author: wei·man cui
 */
@ApiServer("http://localhost:8080/user")
public interface IUserService {

    /**
     * 获取集合 Flux
     *
     * @return 用户集合
     */
    @GetMapping("/")
    Flux<User> listUser();

    /**
     * 根据ID获取
     *
     * @param id id
     * @return user
     */
    @GetMapping("/{id}")
    Mono<User> getUserById(@PathVariable("id") String id);

}

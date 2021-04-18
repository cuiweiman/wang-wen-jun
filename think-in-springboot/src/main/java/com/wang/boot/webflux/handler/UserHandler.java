package com.wang.boot.webflux.handler;

import com.wang.boot.webflux.domain.User;
import com.wang.boot.webflux.service.impl.UserServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @description: 请求处理器，编写后，使用 Router 实现路由，创建一个类似于 Controller 的接口服务
 * @date: 2021/4/17 19:12
 * @author: wei·man cui
 */
@Component
public class UserHandler {

    private final UserServiceImpl userService = new UserServiceImpl();

    public Mono<ServerResponse> listUser(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userService.listUser()));
    }


    public Mono<ServerResponse> getUser(ServerRequest request) {
        String id = request.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userService.getUser(id)));
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        Mono<User> userMono = request.bodyToMono(User.class);
        return userMono.flatMap(user ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(userService.saveUser(userMono.block()))));

        /*return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userService.saveUser(userMono.block())));*/
    }

    public Mono<ServerResponse> deleteById(ServerRequest request) {
        String id = request.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userService.deleteById(id)));
    }

}

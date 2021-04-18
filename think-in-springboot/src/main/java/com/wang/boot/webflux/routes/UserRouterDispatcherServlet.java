package com.wang.boot.webflux.routes;

import com.wang.boot.webflux.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @description: 将 {@link UserHandler} 中的方法添加到服务路径中
 * @date: 2021/4/17 19:42
 * @author: wei·man cui
 */
@Configuration
public class UserRouterDispatcherServlet {
    @Bean
    public RouterFunction<ServerResponse> userRouter(UserHandler handler) {
        return RouterFunctions
                // 服务接口的 前缀路径
                .nest(RequestPredicates.path("/user"), RouterFunctions.route(
                        RequestPredicates.GET("/"),
                        handler::listUser)
                )
                .andRoute(RequestPredicates.GET("/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        handler::getUser
                )
                .andRoute(RequestPredicates.POST("/").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        handler::createUser
                )
                .andRoute(RequestPredicates.DELETE("/{id}"),
                        handler::deleteById
                );
    }

}

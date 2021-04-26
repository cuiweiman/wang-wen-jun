package com.wang.netty.daxin.chapter05rpc.rpcserver;

import com.wang.netty.daxin.chapter05rpc.pojo.RpcRequest;
import com.wang.netty.daxin.chapter05rpc.pojo.RpcResponse;
import com.wang.netty.daxin.chapter05rpc.pojo.RpcService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: Java对象 处理器
 * @author: wei·man cui
 * @date: 2021/4/25 14:39
 */
@Slf4j
public class RpcServerMsgHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> beanMap = new ConcurrentHashMap<>();

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 100, 20, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(256),
            new BasicThreadFactory.Builder().namingPattern("RPC-Netty-Method-Invoke").build()
    );

    public RpcServerMsgHandler() {
        // Dubbo 中是从 Spring 的容器中获取的
        beanMap.put(RpcService.class.getName(), new RpcServiceImpl());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("[服务端 RpcServerMsgHandler#channelRead]接收到数据：{}", msg.toString());
        // 服务端 获取到了 客户端的服务调用请求，根据 反射 实现调用
        RpcRequest request = (RpcRequest) msg;
        // 由于 Netty 的 EventLoop 线程不能处理业务，所以要创建一个线程池去处理耗时的业务逻辑。
        executor.submit(() -> {
            try {
                final Object target = beanMap.get(request.getClassName());
                final Method method = target.getClass().getMethod(request.getMethodName(), request.getParamsClass());
                final Object result = method.invoke(target, request.getParams());
                // 封装结果，并返回 客户端 Netty
                RpcResponse response = new RpcResponse();
                response.setRespId(request.getReqId());
                response.setResult(result);
                ctx.channel().writeAndFlush(response);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
        /*User user = (User) msg;
        user.setAge(user.getAge() + 18);
        ctx.writeAndFlush(user);*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[服务端 RpcServerMsgHandler#exceptionCaught]发生异常： {}", cause.toString());
        ctx.close();
    }
}

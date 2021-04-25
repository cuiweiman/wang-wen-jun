package com.wang.netty.daxin.chapter05rpc.rpcclient;

import com.wang.netty.daxin.chapter05rpc.pojo.RpcService;
import com.wang.netty.daxin.chapter05rpc.pojo.RpcServiceProxyHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;

/**
 * 动态代理，代理 接口服务，不需要实现 接口的方法，完成接口的调用。
 * <p>
 * 当在 {@link #main}中执行{@code rpcService.rpcLogin}时，
 * 实际上调用的是 {@link RpcServiceProxyHandler#invoke} 方法。
 * <p>
 * 需要将 调用的方法和参数，发送给 Netty的服务端(生产者)。
 *
 * @description: 利用 JDK 动态代理，生成 {@link RpcService} 接口的实现类。
 * 不管调用了 接口的  什么方法，都会进入 {@link RpcServiceProxyHandler#invoke} 方法中
 * @author: wei·man cui
 * @date: 2021/4/25 16:19
 */
@Slf4j
public class RpcClientFactory {

    /**
     * Netty 客户端连接，向服务端传递 要调用的 接口名、方法名、方法入参 等信息，
     * 并返回 服务生产者执行后的结果
     *
     * @param ip   ip
     * @param port port
     */
    public RpcClientFactory(String ip, Integer port) {
        final EventLoopGroup loopGroup = new NioEventLoopGroup();
        try {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("ObjectEncoder", new ObjectEncoder());
                            ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.weakCachingResolver(null)));
                            ch.pipeline().addLast(new RpcClientMsgHandler());
                        }
                    });
            final ChannelFuture future = bootstrap.connect(ip, port).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 定义好 {@link RpcService} 接口后，不能手动实现该接口，而是动态生成该接口的实现。
     *
     * @return RpcServer
     */
    public RpcService getRpcService() {
        // 生成 JDK 的动态代理
        return (RpcService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{RpcService.class},
                new RpcServiceProxyHandler()
        );
    }


    public static void main(String[] args) {
        RpcClientFactory factory = new RpcClientFactory("127.0.0.1", 9090);
        final RpcService rpcService = factory.getRpcService();
        rpcService.rpcLogin("Jack", "123");
    }


}

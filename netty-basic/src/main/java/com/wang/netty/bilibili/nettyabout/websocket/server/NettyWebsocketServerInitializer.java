package com.wang.netty.bilibili.nettyabout.websocket.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/2/25 17:14
 */
public class NettyWebsocketServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 基于 Http协议，因此 使用Http编解码器
        pipeline.addLast(new HttpServerCodec());

        // 以 块方式 写，添加 ChunkedWriteHandler处理器
        pipeline.addLast(new ChunkedWriteHandler());

        /*
         * Http数据传输过程中是分段的，HttpObjectAggregator 将多个段聚合起来；
         */
        pipeline.addLast(new HttpObjectAggregator(8192));

        /*
         * WebSocket 数据 以 帧 的形式传递。浏览器请求时按照 WebSocket 协议 访问 ws://localhost:9999/hello
         * WebSocketServerProtocolHandler：
         *      声明 WebSocket连接的 Uri；
         *      将Http协议升级为ws协议，保持长连接。
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

        // 自定义 业务处理器
        pipeline.addLast(new NettyWebsocketServerHandler());
    }

}

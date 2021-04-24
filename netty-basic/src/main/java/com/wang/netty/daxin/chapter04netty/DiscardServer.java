package com.wang.netty.daxin.chapter04netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;

/**
 * @description: 官网案例：https://netty.io/wiki/user-guide-for-4.x.html
 * @author: wei·man cui
 * @date: 2021/4/22 16:38
 */
public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() {
        // 负责处理 客户端 的连接（ServerSocketChannel）
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 负责 专门处理 IO 事件，一般设置为 256~512 之间（SocketChannel）
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 启动的 引导类，供用户 进行 信息配置
            ServerBootstrap b = new ServerBootstrap();
            // 设置线程组
            b.group(bossGroup, workerGroup)
                    // 设置 IO 模型为：异步 IO 模型
                    .channel(NioServerSocketChannel.class)
                    // 在 Pipeline 中配置 数据的处理器类
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            // 粘包 分隔符，只限于 对 \r\n 换行分隔符的拆分
                            // ch.pipeline().addLast("LineBasedFrameDecoder", new LineBasedFrameDecoder(2048 * 1024));

                            // 固定长度为 5 的粘包分隔符
                            // ch.pipeline().addLast("FixedLengthFrameDecoder", new FixedLengthFrameDecoder(5));

                            // 切分消息：根据 lengthFieldOffset 到 lengthFieldLength 截取到 消息（消息开头是16进制数，表示了后面数据的长度）的长度，然后根据长度向后读取指定长度的数据。
                            /*
                            lengthFieldOffset：消息中数据长度所在的偏移量
                            lengthFieldLength：消息中16进制数据长度的位数
                            initialBytesToStrip：下一个数据包跳过的位数
                             */
                            // ch.pipeline().addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 0));

                            // 粘包 分隔符
                            ByteBuf delimiter = Unpooled.copiedBuffer("]".getBytes());
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(2048 * 1024, delimiter));

                            // StringDecoder 继承自 入站处理器类，加入后即可不用再写 ByteBuf 转 String 的处理逻辑了
                            ch.pipeline().addLast("StringDecoder", new StringDecoder(StandardCharsets.UTF_8));
                            // StringEncoder 继承自 出站处理器类，加入后 不用再写 String 转 ByteBuf 的处理逻辑
                            ch.pipeline().addLast("StringEncoder", new StringEncoder(StandardCharsets.UTF_8));
                            ch.pipeline().addLast(new DiscardServerHandler());
                            ch.pipeline().addLast("SelfHandlerName", new SecondHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture future = b.bind(port).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            // 阻塞当前线程，直到 Server 端关闭，才返回线程，继续向下执行
            // future.channel().closeFuture().sync();

            // 使用监听器的形式，关闭 future
            future.channel().closeFuture().addListener(channelFuture -> {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
                System.out.println("[服务端]TCP服务链路关闭。");
            });

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 9090;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new DiscardServer(port).run();
    }

}

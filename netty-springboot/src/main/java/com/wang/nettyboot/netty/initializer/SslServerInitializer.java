package com.wang.nettyboot.netty.initializer;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.io.File;

/**
 * Netty SSL 加密
 *
 * @description: JDK自带的 SslEngine
 * @author: wei·man cui
 * @date: 2021/4/30 17:17
 */
@Slf4j
public class SslServerInitializer extends ChannelInitializer {

    private static SslContext sslContext;

    static {
        // 证书、私钥的地址
        File certChainFile = new File("/home/app/nginx.crt");
        File keyFile = new File("/home/app/pkcs8_rsa_private_key.pem");
        try {
            sslContext = SslContextBuilder.forServer(certChainFile, keyFile)
                    .clientAuth(ClientAuth.NONE)
                    // 优化前，使用 JDK的 SSLEngine
                    // .sslProvider(SslProvider.JDK).build();
                    // 优化后，使用 OpenSSL
                    .sslProvider(SslProvider.OPENSSL).build();
        } catch (SSLException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // SSL 编解码
        ch.pipeline().addLast("ssl", sslContext.newHandler(ByteBufAllocator.DEFAULT));
        // Http 编解码
        ch.pipeline().addLast("http", new HttpServerCodec());
        // Http 协议包收集器
        ch.pipeline().addLast("HttpObjectAggregator", new HttpObjectAggregator(Integer.MAX_VALUE));
        ch.pipeline().addLast("HttpHandler", new HttpHandler());
    }
}

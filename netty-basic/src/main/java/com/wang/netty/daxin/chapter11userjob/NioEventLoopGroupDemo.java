package com.wang.netty.daxin.chapter11userjob;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @description: NioEventLoopGroup 处理用户任务
 * @author: wei·man cui
 * @date: 2021/4/29 10:56
 */
@Slf4j
public class NioEventLoopGroupDemo {

    public static void main(String[] args) {
        final NioEventLoopGroup loopGroup = new NioEventLoopGroup(1);
        // 一个普通的任务，立即执行，只执行一次
        loopGroup.submit(() -> {
            log.debug("submit");
        });
        // 延迟任务，可以设置延迟时间，只执行一次
        loopGroup.schedule(() -> {
            log.debug("schedule 10 Seconds");
        }, 10, TimeUnit.SECONDS);

        // 延迟周期任务，可以设置延迟时间和执行间隔，重复执行。
        loopGroup.scheduleAtFixedRate(() -> {
            log.debug("延迟周期性任务，延迟时间为 {} 秒，执行周期为 {} 秒", 5, 5);
        }, 5, 5, TimeUnit.SECONDS);
    }

}

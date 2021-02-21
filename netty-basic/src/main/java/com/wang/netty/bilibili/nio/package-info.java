/**
 * @description: NIO 非阻塞IO，主要包含三部分：Buffer缓存数组、Channel、Selector。
 * 一个Selector可以管理多个 Channel，Channel之间通过Buffer进行数据块儿传输。
 * 面向缓冲区，将数据读取到缓冲区；非阻塞模式读，线程向通道中发送读请求或者写请求，
 * 即使没有读取到数据或数据没有完全写入，也不会阻塞，可以执行其他任务。
 * @date: 2021/2/21 22:23
 * @author: wei·man cui
 */
package com.wang.netty.bilibili.nio;
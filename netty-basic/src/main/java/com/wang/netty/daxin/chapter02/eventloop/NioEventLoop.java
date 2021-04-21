package com.wang.netty.daxin.chapter02.eventloop;

import com.wang.netty.daxin.chapter02.channel.NioChannel;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @description: 管理 监听读写事件 的注册，并对 监听到的 读写事件 进行分发
 * @author: wei·man cui
 * @date: 2021/4/21 18:06
 */
public class NioEventLoop implements Runnable {

    public Selector selector;

    private final Thread thread;

    private final Queue<Runnable> taskQueue = new LinkedBlockingQueue<>(32);


    public NioEventLoop() {
        try {
            selector = SelectorProvider.provider().openSelector();
            thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 channel 的 监听事件 注册到 selector 中。
     * <p>
     * 在对 selector 进行事件注册时，{@code int eventNum = selector.select();}
     * 使得 线程发生了阻塞，无法 使用本线程 继续 进行事件的注册。
     * <p>
     * 解决方法：
     * 将注册 任务放在 taskQueue 队列中，使用 本类中的 thread 处理 线程任务。
     * <p>
     * {@link Selector#wakeup()} 使{@link NioEventLoop#run}中尚未返回的第一个选择操作立即返回。
     * 如果另一个线程正阻塞在 select() 或 select(long) 方法的调用中，则调用本方法将其立即返回。
     *
     * @param channel channel
     * @param keyOps  监听的事件
     */
    public void register(SocketChannel channel, int keyOps) {
        // 将 注册监听事件 的任务 封装到 线程任务中
        taskQueue.add(() -> {
            try {
                NioChannel nioChannel = new NioChannel(channel, this);
                final SelectionKey selectionKey = channel.register(selector, keyOps);
                // 附加 NioChannel 对象，其中保存了 SocketChannel 和 当前 NioEventLoop 对象
                // 便于 NioEventLoop 事件监听 和 读写分发 的解耦
                selectionKey.attach(nioChannel);
            } catch (ClosedChannelException e) {
                throw new RuntimeException(e);
            }
        });
        // 唤醒 下面 run() 线程中 Selector#select 方法阻塞的 线程
        // 使得 该线程继续向下执行，遍历 taskQueue 队列，完成 监听事件的注册
        selector.wakeup();
    }

    @Override
    public void run() {
        // 死循环：Thread 处于 非中断 状态
        while (!Thread.interrupted()) {
            try {
                System.out.println(thread + " 开始轮询监听 IO 事件");
                // 本方法 会发生 线程 阻塞
                int eventNum = selector.select();
                if (eventNum > 0) {
                    // 发生了 IO 事件
                    System.out.println("[服务器]发生了IO事件，数量：" + eventNum);
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        NioChannel nioChannel = (NioChannel) selectionKey.attachment();
                        if (selectionKey.isReadable()) {
                            nioChannel.read(selectionKey);
                        } else if (selectionKey.isWritable()) {
                            nioChannel.write(selectionKey);
                        }
                        iterator.remove();
                    }
                }
                Runnable task;
                while (Objects.nonNull(task = taskQueue.poll())) {
                    // 普通方法 的方式调用
                    task.run();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

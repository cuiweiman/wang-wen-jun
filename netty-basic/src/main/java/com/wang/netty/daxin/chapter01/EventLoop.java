package com.wang.netty.daxin.chapter01;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @description: 监听事件
 * @date: 2021/4/20 23:22
 * @author: wei·man cui
 */
public class EventLoop implements Runnable {

    private Selector selector;

    private Thread thread;

    private Queue<Runnable> taskQueue = new LinkedBlockingQueue<>(32);

    public EventLoop() throws IOException {
        this.selector = SelectorProvider.provider().openSelector();
        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * 将 监听 channel 的事件 注册到 selector 中。
     * <p>
     * 在对 selector 进行事件注册时，{@code int eventNum = selector.select();}
     * 使得 {@code thread}线程发生了阻塞，无法 使用本线程 进行事件的注册。
     * <p>
     * 解决方法：
     * 将注册 任务放在 taskQueue 队列中，使用 本类中的 thread 处理 线程任务。
     * <p>
     * {@link Selector#wakeup()} 使尚未范围的第一个选择操作立即返回。
     * 如果另一个线程正阻塞在 select() 或 select(long) 方法的调用中，则调用本方法将其立即返回。
     *
     * @param channel channel
     * @param keyOps  监听的事件
     */
    public void register(SocketChannel channel, int keyOps) {
        // 将 读事件 监听 封装到 线程任务中
        taskQueue.add(() -> {
            try {
                channel.register(selector, keyOps);
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        });
        selector.wakeup();
    }

    @Override
    public void run() {
        // 死循环：Thread 处于 非中断 状态
        while (!Thread.interrupted()) {
            try {
                System.out.println(thread + "开始轮询监听IO事件");
                int eventNum = selector.select();
                if (eventNum > 0) {
                    // eventNum <= 0 意味着 没有 IO 事件发生，或者 执行了 selector.wakeup() 方法
                    System.out.println("系统发生IO事件 数量：——> " + eventNum);
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey selectionKey = keyIterator.next();
                        if (selectionKey.isReadable()) {
                            // 可读事件
                            SocketChannel channel = null;
                            try {
                                channel = (SocketChannel) selectionKey.channel();
                                final ByteBuffer buffer = ByteBuffer.allocate(1024);
                                final int read = channel.read(buffer);
                                if (read > 0) {
                                    String msg = new String(buffer.array());
                                    System.out.println("[服务端]接收到客户端消息：" + msg);
                                }
                                selectionKey.attach("hello client".getBytes());
                                // 切换 监听事件 为：写事件
                                selectionKey.interestOps(SelectionKey.OP_WRITE);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if (selectionKey.isWritable()) {
                            SocketChannel channel = null;
                            try {
                                channel = (SocketChannel) selectionKey.channel();
                                ByteBuffer buffer = ByteBuffer.wrap("write msg".getBytes());
                                int write = channel.write(buffer);
                                System.out.println("写入消息：" + write);
                                // 写完后 在切换为 监听可读事件
                                selectionKey.interestOps(SelectionKey.OP_READ);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    keyIterator.remove();
                }
                // 执行 监听事件注册 的 线程任务
                Runnable task;
                while ((task = taskQueue.poll()) != null) {
                    task.run();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}

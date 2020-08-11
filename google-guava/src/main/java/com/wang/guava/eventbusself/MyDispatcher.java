package com.wang.guava.eventbusself;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * @description: 负责 分发 事件总线中的事件给 订阅者
 * @date: 2020/8/11 23:25
 * @author: wei·man cui
 */
public class MyDispatcher {

    private final Executor executorService;

    private final MyEventExceptionHandler exceptionHandler;

    public static final Executor SEQ_EXECUTOR_SERVICE = SeqExecutorService.INSTANCE;

    public static final Executor PRE_THREAD_EXECUTOR_SERVICE = PreThreadExecutorService.INSTANCE;

    private MyDispatcher(Executor executorService, MyEventExceptionHandler exceptionHandler) {
        this.executorService = executorService;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * 事件 分发给 订阅者
     *
     * @param bus        事件 上下文
     * @param myRegister 订阅者存储类
     * @param event      事件
     * @param topic      订阅的主题
     */
    public void dispatch(Bus bus, MyRegister myRegister, Object event, String topic) {


    }

    /**
     * 如果是 线程资源，则关闭
     */
    public void close() {
        if (executorService instanceof ExecutorService) {
            ((ExecutorService) executorService).shutdown();
        }
    }

    static MyDispatcher newDispatcher(Executor executor, MyEventExceptionHandler handler) {
        return new MyDispatcher(executor, handler);
    }

    static MyDispatcher seqDispatcher(MyEventExceptionHandler handler) {
        return new MyDispatcher(SEQ_EXECUTOR_SERVICE, handler);
    }

    static MyDispatcher preDispatcher(MyEventExceptionHandler handler) {
        return new MyDispatcher(PRE_THREAD_EXECUTOR_SERVICE, handler);
    }

    /**
     * 将系统总线的事件，串行交给订阅者。单例
     */
    private static class SeqExecutorService implements Executor {
        private final static SeqExecutorService INSTANCE = new SeqExecutorService();

        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }

    private static class PreThreadExecutorService implements Executor {
        private final static PreThreadExecutorService INSTANCE = new PreThreadExecutorService();

        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    }
}

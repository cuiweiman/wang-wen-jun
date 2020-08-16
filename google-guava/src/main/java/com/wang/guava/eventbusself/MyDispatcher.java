package com.wang.guava.eventbusself;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * @description: 负责 分发 事件总线中的事件给 订阅者
 * @date: 2020/8/11 23:25
 * @author: wei·man cui
 */
public class MyDispatcher {

    /**
     * 多线程服务 执行任务
     */
    private final Executor executorService;

    /**
     * 异常处理
     */
    private final MyEventExceptionHandler exceptionHandler;

    public static final Executor SEQ_EXECUTOR_SERVICE = SeqExecutorService.INSTANCE;

    public static final Executor PER_THREAD_EXECUTOR_SERVICE = PerThreadExecutorService.INSTANCE;

    private MyDispatcher(Executor executorService, MyEventExceptionHandler exceptionHandler) {
        this.executorService = executorService;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * 事件 分发给 订阅者
     *
     * @param bus        事件总线
     * @param myRegister 订阅者存储类
     * @param event      事件
     * @param topic      订阅的主题
     */
    public void dispatch(Bus bus, MyRegister myRegister, Object event, String topic) {
        ConcurrentLinkedQueue<MySubscriber> mySubscribers = myRegister.scanSubscriber(topic);
        if (null == mySubscribers) {
            if (exceptionHandler != null) {
                exceptionHandler.handle(new IllegalArgumentException("The topic " + topic + " not bind yet"),
                        new BaseMyEventContext(bus.getBusName(), null, event));
            }
            return;
        }
        mySubscribers.stream().filter(mySubscriber -> !mySubscriber.isDisable())
                .filter(mySubscriber -> {
                    Method method = mySubscriber.getSubscribeMethod();
                    Class<?> aClass = method.getParameterTypes()[0];
                    return aClass.isAssignableFrom(event.getClass());
                }).forEach(mySubscriber -> realInvokeSubscribe(mySubscriber, event, bus));
    }

    private void realInvokeSubscribe(MySubscriber mySubscriber, Object event, Bus bus) {
        Method subscribeMethod = mySubscriber.getSubscribeMethod();
        Object subscribeObject = mySubscriber.getSubscribeObject();
        executorService.execute(() -> {
            try {
                subscribeMethod.invoke(subscribeObject, event);
            } catch (Exception e) {
                if (null != exceptionHandler) {
                    exceptionHandler.handle(e, new BaseMyEventContext(bus.getBusName(), mySubscriber, event));
                }
            }
        });
    }

    /**
     * 如果是 ExecutorService 资源，则需要关闭
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
        return new MyDispatcher(PER_THREAD_EXECUTOR_SERVICE, handler);
    }

    /**
     * 串行：将系统总线的事件，转交给订阅者。单例-饿汉式
     */
    private static class SeqExecutorService implements Executor {
        private final static SeqExecutorService INSTANCE = new SeqExecutorService();

        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }

    //FIXME
    private static class PerThreadExecutorService implements Executor {
        private final static PerThreadExecutorService INSTANCE = new PerThreadExecutorService();

        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    }

    private static class BaseMyEventContext implements MyEventContext {
        private final String eventBusName;

        private final MySubscriber subscriber;

        private final Object event;

        public BaseMyEventContext(String eventBusName, MySubscriber subscriber, Object event) {
            this.eventBusName = eventBusName;
            this.subscriber = subscriber;
            this.event = event;
        }

        @Override
        public String getSource() {
            return this.eventBusName;
        }

        @Override
        public Object getSubscriber() {
            return subscriber != null ? subscriber.getSubscribeObject() : null;
        }

        @Override
        public Method getSubscribe() {
            return subscriber != null ? subscriber.getSubscribeMethod() : null;
        }

        @Override
        public Object getEvent() {
            return this.event;
        }
    }

}

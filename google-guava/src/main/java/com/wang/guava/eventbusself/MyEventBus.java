package com.wang.guava.eventbusself;

import java.util.concurrent.Executor;

/**
 * @description: 自定义 EventBus 接口实现类
 * @date: 2020/8/11 23:08
 * @author: wei·man cui
 */
public class MyEventBus implements Bus {

    private final MyRegister myRegister = new MyRegister();

    private String busName;

    private final static String DEFAULT_BUS_NAME = "default";

    private final static String DEFAULT_TOPIC = "default-topic";

    private final MyDispatcher dispatcher;

    private final MyEventExceptionHandler exceptionHandler;

    public MyEventBus() {
        this(DEFAULT_BUS_NAME, null, MyDispatcher.SEQ_EXECUTOR_SERVICE);
    }

    public MyEventBus(String busName) {
        this(busName, null, MyDispatcher.SEQ_EXECUTOR_SERVICE);
    }

    public MyEventBus(MyEventExceptionHandler exceptionHandler) {
        this(DEFAULT_BUS_NAME, exceptionHandler, MyDispatcher.SEQ_EXECUTOR_SERVICE);
    }

    public MyEventBus(String busName, MyEventExceptionHandler exceptionHandler, Executor executor) {
        this.busName = busName;
        this.exceptionHandler = exceptionHandler;
        this.dispatcher = MyDispatcher.newDispatcher(executor, exceptionHandler);
    }

    @Override
    public void register(Object subscribe) {
        this.myRegister.bind(subscribe);
    }

    @Override
    public void unregister(Object subscribe) {
        this.myRegister.unbind(subscribe);
    }

    @Override
    public void post(Object event) {
        this.post(event, DEFAULT_TOPIC);
    }

    @Override
    public void post(Object event, String topic) {
        this.dispatcher.dispatch(this, myRegister, event, topic);
    }

    @Override
    public void close() {
        this.dispatcher.close();
    }

    @Override
    public String getBusName() {
        return this.busName;
    }
}

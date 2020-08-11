package com.wang.guava.eventbusself;

/**
 * @description: 事件处理异常接口
 * @date: 2020/8/11 23:22
 * @author: wei·man cui
 */
public interface MyEventExceptionHandler {

    /**
     * 事件异常处理
     *
     * @param cause
     * @param context
     */
    void handle(Throwable cause, MyEventContext context);

}

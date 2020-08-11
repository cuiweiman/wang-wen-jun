package com.wang.guava.eventbusself;

/**
 * @description: 自定义 EventBus总线接口
 * @date: 2020/8/11 23:03
 * @author: wei·man cui
 */
public interface Bus {

    /**
     * 注册订阅者
     *
     * @param subscribe 订阅者
     */
    void register(Object subscribe);

    /**
     * 订阅者 取消注册
     *
     * @param subscribe 订阅者
     */
    void unregister(Object subscribe);

    /**
     * 发送事件到 EventBus中
     *
     * @param event 事件
     */
    void post(Object event);

    /**
     * 发送事件到 EventBus的指定 Topic中
     *
     * @param event 事件
     * @param topic 主题
     */
    void post(Object event, String topic);

    /**
     * 进行资源释放
     */
    void close();

    /**
     * 总线名字
     *
     * @return 总线名字
     */
    String getBusName();

}

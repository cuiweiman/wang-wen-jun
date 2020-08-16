package com.wang.guava.eventbusself;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <p>
 * 一个topic ——> 可以对应多个 Subscriber 订阅者。
 * </p>
 *
 * @description: 注册表类 保存Subscribe订阅者信息
 * @date: 2020/8/11 23:09
 * @author: wei·man cui
 */
class MyRegister {

    /**
     * 存储 订阅者 与 事件总线中topic 的绑定关系.
     * String 事件总线 Topic
     * ConcurrentLinkedQueue<Subscriber> 订阅者队列。
     * 由于 topic 与 订阅者 是一对多的关系，因此使用 线程安全的队列 存储Topic的订阅者
     */
    private final Map<String, ConcurrentLinkedQueue<MySubscriber>> subscriberContainer = new ConcurrentHashMap<>();

    /**
     * 将 事件总线 与 订阅者 绑定
     *
     * @param subscribe 订阅者
     */
    public void bind(Object subscribe) {
        List<Method> subscribeMethods = getSubscribeMethods(subscribe);
        subscribeMethods.forEach(m -> tigerSubscriber(subscribe, m));
    }

    /**
     * 将 事件总线 与 订阅者 解绑
     *
     * @param subscriber 订阅者
     */
    public void unbind(Object subscriber) {
        subscriberContainer.forEach((key, queue) -> {
            queue.forEach(s -> {
                if (s.getSubscribeObject() == subscriber) {
                    s.setDisable(true);
                }
            });
        });
    }

    public ConcurrentLinkedQueue<MySubscriber> scanSubscriber(final String topic) {
        return subscriberContainer.get(topic);
    }

    private void tigerSubscriber(Object subscriber, Method method) {
        MySubscribe mySubscribe = method.getDeclaredAnnotation(MySubscribe.class);
        String topic = mySubscribe.topic();
        subscriberContainer.computeIfAbsent(topic, key -> new ConcurrentLinkedQueue<>());
        // 上一行代码 等价于 如下if语句
        /*ConcurrentLinkedQueue<MySubscriber> mySubscribers = subscriberContainer.get(topic);
        if (mySubscribers == null) {
            mySubscribers = new ConcurrentLinkedQueue<>();
            subscriberContainer.put(topic, mySubscribers);
        }*/
        subscriberContainer.get(topic).add(new MySubscriber(subscriber, method));
    }

    /**
     * 获取订阅者类的 所有父类
     * 子订阅者 订阅事件总线的Topic后，其父订阅者也会订阅
     *
     * @param subscribe 订阅者
     * @return 订阅者及其继承的父类
     */
    private List<Method> getSubscribeMethods(Object subscribe) {
        final List<Method> methods = new ArrayList<>();
        Class<?> temp = subscribe.getClass();
        while (temp != null) {
            Method[] declaredMethods = temp.getDeclaredMethods();
            Arrays.stream(declaredMethods)
                    .filter(m -> m.isAnnotationPresent(MySubscribe.class) && m.getParameterCount() == 1 && m.getModifiers() == Modifier.PUBLIC)
                    .forEach(methods::add);
            temp = temp.getSuperclass();
        }
        return methods;
    }
}

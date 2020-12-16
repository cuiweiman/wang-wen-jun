package com.wang.think.lookupmethod;

/**
 * @description: 创建调用方法
 * @date: 2020/12/16 22:39
 * @author: wei·man cui
 */
public abstract class GetBeanTest {

    public void showMe() {
        this.getBean().showMe();
    }

    /**
     * 获取 Bean 实例，可能是父类是 User 的任一子类，配置在 XML 的 lookup-method 属性中
     *
     * @return bean实例
     */
    public abstract User getBean();
}

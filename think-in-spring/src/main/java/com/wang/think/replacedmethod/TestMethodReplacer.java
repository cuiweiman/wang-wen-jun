package com.wang.think.replacedmethod;

import org.springframework.beans.factory.support.MethodReplacer;

import java.lang.reflect.Method;

/**
 * @description: 在运营一段时间后，需要改变原有的业务逻辑，配置 XLM 文档，可以使本替换类生效。
 * @date: 2020/12/16 22:59
 * @author: wei·man cui
 */
public class TestMethodReplacer implements MethodReplacer {

    @Override
    public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
        System.out.println(" 我替换了 原有的方法 ");
        return null;
    }
}

package com.wang.guava.cache.guavacache;

import com.google.common.base.MoreObjects;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 需要缓存的对象
 * @author: wei·man cui
 * @date: 2020/8/21 13:40
 */
@Getter
@AllArgsConstructor
public class Employee {

    private final String name;

    private final String dept;

    private final String empId;

    private final byte[] data = new byte[1024 * 1024];

    /**
     * 垃圾回收时，与根索引的引用断开后，会执行finalize()方法，被标记为可回收
     *
     * @throws Throwable 错误
     */
    @Override
    protected void finalize() throws Throwable {
        System.out.println("The name 【 " + this.name + " will be GC 】");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Name", this.getName())
                .add("Department", this.getDept())
                .add("EmployeeID", this.getEmpId())
                .toString();
    }
}

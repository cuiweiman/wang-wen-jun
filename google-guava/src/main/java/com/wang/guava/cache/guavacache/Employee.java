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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Name", this.getName())
                .add("Department", this.getDept())
                .add("EmployeeID", this.getEmpId())
                .toString();
    }
}

package com.wang.basic.unsafe;

/**
 * @description: 实体类
 * @author: wei·man cui
 * @date: 2021/2/4 14:40
 */
public class EntityDomain {

    private String name;

    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public EntityDomain() {
    }

    public EntityDomain(String name, String address) {
        this.name = name;
        this.address = address;
    }
}

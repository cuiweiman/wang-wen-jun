package com.wang.netty.daxin.chapter05rpc.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @description: Java对象
 * @author: wei·man cui
 * @date: 2021/4/25 14:50
 */
@Data
@NoArgsConstructor
public class User implements Serializable {
    private String name;
    private Integer age;
    private String birthDay;

    public User(boolean flag) {
        if (flag) {
            this.name = "Jack";
            this.age = 18;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
            this.birthDay = sdf.format(new Date());
        }
    }

    @Override
    public String toString() {

        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", birthDay=" + birthDay +
                '}';
    }
}

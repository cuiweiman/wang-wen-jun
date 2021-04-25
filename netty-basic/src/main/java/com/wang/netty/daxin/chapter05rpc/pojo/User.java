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
    private Date birthDay;

    public User(boolean flag) {
        User user = new User();
        if (flag) {
            user.setName("Jack");
            user.setAge(18);
            user.setBirthDay(new Date());
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
        final String format = sdf.format(this.birthDay);
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", birthDay=" + format +
                '}';
    }
}

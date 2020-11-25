package com.wang.es.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: es 数据对象
 * @date: 2020/11/22 22:20
 * @author: wei·man cui
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private String name;

    private Integer age;

}

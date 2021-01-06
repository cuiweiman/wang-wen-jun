package com.wang.basic.passencode;

import cn.hutool.crypto.digest.MD5;

/**
 * @description: 密码加密
 * @author: wei·man cui
 * @date: 2021/1/6 11:21
 */
public class PassEncodeDemo {
    public static void main(String[] args) {
        String encode = "0192023a7bbd73250516f069df18b500";
        String real = "admin123";
        String md5 = MD5.create().digestHex(real);
        System.out.println(encode.equalsIgnoreCase(md5));
    }
}

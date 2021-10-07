package com.wang.wx.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: global exception
 * @author: cuiweiman
 * @date: 2021/10/7 13:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GlobalException extends RuntimeException {

    private final Integer code;

    private final String msg;

    public GlobalException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public GlobalException(Integer code, String msg, Throwable cause) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

}

package com.wang.wx.enums;

import lombok.Getter;

/**
 * @description: response status
 * @author: cuiweiman
 * @date: 2021/10/7 13:14
 */
@Getter
public enum ResponseStatus {

    /**
     * success or error
     */
    SUCCESS(200, "success"),
    SYSTEM_ERROR(500, "system error"),

    ;

    private Integer code;
    private String msg;

    ResponseStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

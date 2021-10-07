package com.wang.wx.response;

import com.wang.wx.enums.ResponseStatus;
import com.wang.wx.exception.GlobalException;
import lombok.Builder;
import lombok.Data;

/**
 * @description: for response
 * @author: cuiweiman
 * @date: 2021/10/7 12:59
 */
@Data
@Builder
public class ResultVO {

    private Integer code;

    private String msg;

    private Object data;

    public static ResultVO success(Integer code, String msg) {
        return ResultVO.builder().code(code).msg(msg).build();
    }

    public static ResultVO success(Integer code, String msg, Object data) {
        return ResultVO.builder().code(code).msg(msg).data(data).build();
    }

    public static ResultVO success(ResponseStatus status, Object data) {
        return ResultVO.builder().code(status.getCode()).msg(status.getMsg()).data(data).build();
    }

    public static ResultVO success(Object data) {
        return success(ResponseStatus.SUCCESS, data);
    }


    public static ResultVO fail(Integer code, String msg) {
        return ResultVO.builder().code(code).msg(msg).build();
    }

    public static ResultVO fail(GlobalException e) {
        return fail(e.getCode(), e.getMsg());
    }


}

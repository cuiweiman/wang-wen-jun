package com.wang.kafka.wechat.model.response;

import com.wang.kafka.wechat.exception.GlobalException;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * @description: for response
 * @author: cuiweiman
 * @date: 2021/10/7 12:59
 */
@Data
@Builder
public class ResultVO {

    private String requestId;

    private Integer code;

    private String msg;

    private Object  data;

    public static ResultVO success() {
        return success(200, "success");
    }

    public static ResultVO success(Integer code, String msg) {
        return ResultVO.builder().requestId(getRequestId()).code(code).msg(msg).build();
    }

    public static <T> ResultVO success(Integer code, String msg, T data) {
        return ResultVO.builder().requestId(getRequestId()).code(code).msg(msg).data(data).build();
    }

    public static <T> ResultVO success(T data) {
        return ResultVO.builder().requestId(getRequestId()).code(200).msg("success").data(data).build();
    }

    public static ResultVO fail(GlobalException e) {
        return fail(e.getCode(), e.getMsg());
    }

    public static ResultVO fail(Integer code, String msg) {
        return ResultVO.builder().requestId(getRequestId()).code(code).msg(msg).build();
    }

    private static String getRequestId() {
        return UUID.randomUUID().toString();
    }

}

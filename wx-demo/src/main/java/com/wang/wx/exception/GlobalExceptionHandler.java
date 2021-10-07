package com.wang.wx.exception;

import com.wang.wx.model.response.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Desc 全局异常处理
 * @Author cui·weiman
 * @Since 2021/10/7 18:21
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = GlobalException.class)
    public ResultVO bizExceptionHandler(GlobalException e) {
        return ResultVO.fail(e.getCode(), e.getMsg());
    }

}

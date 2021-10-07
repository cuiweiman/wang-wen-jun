package com.wang.wx.controller;

import com.wang.wx.response.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: for auth
 * @author: cuiweiman
 * @date: 2021/10/7 12:57
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/test")
    public ResultVO test() {
        String result = "SunYuYing will marry CuiWeiMan";
        return ResultVO.success(result);
    }


}

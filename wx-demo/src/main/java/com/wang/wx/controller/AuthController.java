package com.wang.wx.controller;

import com.wang.wx.model.response.*;
import com.wang.wx.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: for auth
 * @author: cuiweiman
 * @date: 2021/10/7 12:57
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "Wechat用户认证模块")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    private final AuthService authService;

    @GetMapping("/test")
    @ApiOperation(value = "测试接口")
    public ResultVO test() {
        String result = "SunYuYing will marry CuiWeiMan";
        return ResultVO.success(result);
    }


    @GetMapping("/check")
    @ApiOperation(value = "校验 accessToken")
    public ResultVO checkAccessToken(@ApiParam(value = "openid", required = true) @RequestParam String openid,
                                     @ApiParam(value = "accessToken", required = true) @RequestParam String accessToken) {
        CheckAccessTokenResponse checkAccessTokenResponse = authService.checkAccessToken(openid, accessToken);
        return ResultVO.success(checkAccessTokenResponse);
    }


    @GetMapping("/token")
    @ApiOperation(value = "获取 access token")
    public ResultVO getAccessToken(@ApiParam(value = "微信Code", required = true) @RequestParam String code) {
        AccessTokenResponse accessToken = authService.getAccessToken(code);
        return ResultVO.success(accessToken);
    }


    @PutMapping("/token")
    @ApiOperation(value = "刷新 access token")
    public ResultVO refreshAccessToken(
            @ApiParam(value = "refreshToken", required = true) @RequestParam String refreshToken) {
        AccessTokenResponse accessToken = authService.refreshAccessToken(refreshToken);
        return ResultVO.success(accessToken);
    }


    @GetMapping("/ticket")
    @ApiOperation(value = "获取 jsApi ticket")
    public ResultVO getJsApiTicket() {
        JsApiTicketResponse jsApiTicket = authService.getJsApiTicket();
        return ResultVO.success(jsApiTicket);
    }


    @GetMapping("/jssdkconfig")
    @ApiOperation(value = "获取 JS-SDK 配置信息")
    public ResultVO getJsSdkConfig(@ApiParam(value = "openId", required = true) @RequestParam String openId,
                                   @ApiParam(value = "url", required = true) @RequestParam String url) {
        JsSdkConfigResponse jsSdkConfig = authService.getJsSdkConfig(openId, url);
        return ResultVO.success(jsSdkConfig);
    }


    @GetMapping("/info")
    @ApiOperation(value = "获取 用户信息")
    public ResultVO getUserInfo(@ApiParam(value = "openid", required = true) @RequestParam String openid,
                                @ApiParam(value = "accessToken", required = true) @RequestParam String accessToken) {
        WechatUserInfoResponse wechatUserInfoResponse = authService.getUserInfo(openid, accessToken);
        return ResultVO.success(wechatUserInfoResponse);
    }

}

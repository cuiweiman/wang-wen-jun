package com.wang.wx.model.response;

import lombok.Data;

/**
 * @Desc access token
 * @Author cui·weiman
 * @Since 2021/10/7 17:53
 */
@Data
public class AccessTokenResponse {

    private String accessToken;
    private Integer expiresIn;
    private String refreshToken;
    private String openid;
    private String scope;

    private Integer errcode;
    private String errmsg;

    public static AccessTokenResponse empty() {
        AccessTokenResponse response = new AccessTokenResponse();
        response.errcode = 500;
        response.errmsg = "access token 获取为空";
        return response;
    }

}

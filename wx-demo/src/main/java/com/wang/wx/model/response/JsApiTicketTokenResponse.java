package com.wang.wx.model.response;

import lombok.Data;

/**
 * @Desc js api ticket token
 * @Author cui·weiman
 * @Since 2021/10/7 18:57
 */
@Data
public class JsApiTicketTokenResponse {

    private Integer errcode;
    private String errmsg;

    private String accessToken;
    private Integer expiresIn;

    public static JsApiTicketTokenResponse empty() {
        JsApiTicketTokenResponse response = new JsApiTicketTokenResponse();
        response.errcode = 500;
        response.errmsg = "jsApi access token 获取为空";
        return response;
    }
}

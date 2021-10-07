package com.wang.wx.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Desc user info
 * @Author cui·weiman
 * @Since 2021/10/7 20:35
 */
@Data
@ApiModel("微信用户信息")
public class WechatUserInfoResponse {

    private String openid;

    private String nickname;

    @ApiModelProperty("性别：1男,2女,0未知")
    private Integer sex;

    private String province;

    private String city;

    private String country;

    private String headimgurl;

    private String privilege;

    private String unionid;

    private Integer errcode;

    private String errmsg;

}

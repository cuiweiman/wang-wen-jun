package com.wang.wx.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Desc get user info vo
 * @Author cui·weiman
 * @Since 2021/10/7 17:07
 */
@Data
@ApiModel("获取用户信息 VO")
public class GetUserInfoRequest {

    @ApiModelProperty("code")
    private String code;

}

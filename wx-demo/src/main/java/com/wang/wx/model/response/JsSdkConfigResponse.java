package com.wang.wx.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Desc js-sdk config
 * @Author cui·weiman
 * @Since 2021/10/7 21:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("JS-SDK签名配置")
public class JsSdkConfigResponse {

    @ApiModelProperty("生成签名的时间戳")
    private long timestamp;

    @ApiModelProperty("生成签名的随机串")
    private String nonceStr;

    @ApiModelProperty("签名")
    private String signature;

}

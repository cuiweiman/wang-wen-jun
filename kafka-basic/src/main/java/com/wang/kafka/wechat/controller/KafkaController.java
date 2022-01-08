package com.wang.kafka.wechat.controller;

import com.wang.kafka.wechat.model.response.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: for test
 * @author: cuiweiman
 * @date: 2021/10/14 15:25
 */
@RestController
@RequestMapping("/produce")
@Api(tags = "Kafka消息发送模块")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaController {

    @GetMapping("/test")
    @ApiOperation(value = "测试接口")
    public ResultVO test() {
        String result = "kafka";
        return ResultVO.success(result);
    }


}

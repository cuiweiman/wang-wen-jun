package com.wang.kafka.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.wang.kafka.wechat.config.WeChatTemplateProperties;
import com.wang.kafka.wechat.model.response.ResultVO;
import com.wang.kafka.wechat.service.WeChatTemplateService;
import com.wang.kafka.wechat.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @description: WeChat Template
 * @author: cuiweiman
 * @date: 2021/11/8 15:46
 */
@RestController
@RequestMapping("/v1")
@Api(tags = "微信下程序-模板API")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeChatTemplateController {

    private final WeChatTemplateService weChatTemplateService;

    @GetMapping("/template")
    @ApiOperation(value = "获取调查问卷")
    public ResultVO test() {
        WeChatTemplateProperties.WeChatTemplate weChatTemplate = weChatTemplateService.getWeChatTemplate();
        Map<String, Object> result = Maps.newHashMap();
        result.put("templateId", weChatTemplate.getTemplateId());
        result.put("template", FileUtils.readFile2JsonArray(weChatTemplate.getTemplateFilePath()));
        return ResultVO.success(result);
    }

    @PostMapping("/template/report")
    @ApiOperation(value = "上传调查问卷的填写结果")
    public ResultVO dataReported(@RequestBody String reportData) {
        weChatTemplateService.templateReported(JSON.parseObject(reportData));
        return ResultVO.success(true);
    }

    @GetMapping("/template/result")
    @ApiOperation(value = "获取调查问卷的统计结果")
    public ResultVO templateStatistics(@RequestParam(value = "templateId", required = false) String templateId) {
        JSONObject statistics = weChatTemplateService.templateStatistics(templateId);
        return ResultVO.success(statistics);
    }


}

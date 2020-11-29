package com.wang.es.controller;

import com.wang.es.entity.JdGoods;
import com.wang.es.service.impl.JdGoodServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @date: 2020/11/27 0:01
 * @author: wei·man cui
 */
@RestController
@RequestMapping("/goods")
public class JdGoodController {

    @Resource
    private JdGoodServiceImpl jdGoodService;

    @GetMapping("/parse/{keyWord}")
    public boolean parse(@PathVariable String keyWord) throws IOException {
        return jdGoodService.parseContent(keyWord);
    }

    @GetMapping("/search/{keyWord}/{pageNo}/{pageSize}")
    public List<JdGoods> searchPage(@PathVariable String keyWord,
                                     @PathVariable int pageNo,
                                     @PathVariable int pageSize) throws IOException {
        return jdGoodService.searchPage(keyWord, pageNo, pageSize);
    }

    @GetMapping("/searchPageHighLight/{keyWord}/{pageNo}/{pageSize}")
    public List<Map<String, Object>> searchPageHighLight(@PathVariable String keyWord,
                                                @PathVariable int pageNo,
                                                @PathVariable int pageSize) throws IOException {
        return jdGoodService.searchPageHighLight(keyWord, pageNo, pageSize);
    }

}

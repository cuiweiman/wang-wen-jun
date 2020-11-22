package com.wang.es.controller;

import com.wang.es.service.impl.EsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @date: 2020/11/22 22:26
 * @author: wei·man cui
 */
@AllArgsConstructor
@RestController
@RequestMapping("/esSysUser")
public class EsSysUserController {

    private EsServiceImpl esService;

    @PostMapping("/createIndex")
    public Boolean createIndex() {
        return esService.createIndex();
    }

    @PostMapping("/createIndex/{indexName}")
    public Boolean createIndex(@PathVariable String indexName) {
        return esService.createIndex(indexName);
    }

    @DeleteMapping("/{indexName}")
    public Boolean deleteIndex(@PathVariable String indexName) {


        return null;
    }

}

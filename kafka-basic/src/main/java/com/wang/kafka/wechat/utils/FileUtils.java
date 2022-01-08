package com.wang.kafka.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @description: FileUtils
 * @author: cuiweiman
 * @date: 2021/11/8 16:03
 */
@Slf4j
public class FileUtils {

    public static String readFile(String filePath) throws IOException {
        try (InputStream is = new FileInputStream(filePath)) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        }
    }


    public static Optional<JSONObject> readFile2JsonObject(String filePath) {
        try {
            String fileContent = readFile(filePath);
            log.info("readFile2Json fileContent: [{}]", fileContent);
            return Optional.ofNullable(JSON.parseObject(fileContent));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<JSONArray> readFile2JsonArray(String filePath) {
        try {
            String fileContent = readFile(filePath);
            log.info("readFile2Json fileContent: [{}]", fileContent);
            return Optional.ofNullable(JSON.parseArray(fileContent));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}


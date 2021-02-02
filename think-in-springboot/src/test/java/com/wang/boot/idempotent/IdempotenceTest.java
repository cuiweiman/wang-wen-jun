package com.wang.boot.idempotent;

import com.wang.boot.MainApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * @description: 幂等性 Mock测试用例
 * @author: wei·man cui
 * @date: 2021/2/2 11:02
 */
@Slf4j
public class IdempotenceTest extends MainApplicationTest {

    @Resource
    private WebApplicationContext context;

    @Test
    public void idempotentTest() throws Exception {
        final MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        String token = mockMvc.perform(MockMvcRequestBuilders.get("/token").accept(MediaType.TEXT_HTML))
                .andReturn().getResponse().getContentAsString();
        log.info("获得的 Token 串是：{}", token);

        for (int i = 0; i < 5; i++) {
            log.info("第 {} 次调用测试接口", i);
            final String result = mockMvc.perform(MockMvcRequestBuilders.post("/test")
                    .header("token", token).accept(MediaType.TEXT_HTML))
                    .andReturn().getResponse().getContentAsString();
            log.info(result);
            if (i == 0) {
                Assert.assertEquals("正常调用", result);
            } else {
                Assert.assertEquals("重复调用", result);
            }
        }
    }

}

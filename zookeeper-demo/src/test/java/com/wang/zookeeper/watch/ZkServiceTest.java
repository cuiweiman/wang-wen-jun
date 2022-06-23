package com.wang.zookeeper.watch;

import com.wang.zookeeper.ZookeeperApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@Slf4j
@DisplayName("Zookeeper测试")
@SpringBootTest(classes = ZookeeperApplication.class)
public class ZkServiceTest {

    @Autowired
    private ZkService zkService;

    @Test
    void putNodeTtl() throws InterruptedException {
        String path = "/iot-platform/node-ttl";
        String test = zkService.putNodeTtl(path, "test", 5L);
        log.info("path={}", test);

        TimeUnit.SECONDS.sleep(7);
        Boolean exists = zkService.exists(path);
        log.info("path {} exists {}", path, exists);
    }

    @Test
    void exists() {
        String path = "/iot-platform/node-ttl";
        Boolean exists = zkService.exists(path);
        log.info("path {} exists {}", path, exists);
    }
}
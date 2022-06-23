package com.wang.alipay.utils;

import com.wang.alipay.AliPayApplicationTest;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2021/1/4 11:07
 */
public class IdGeneratorTest extends AliPayApplicationTest {

    @Resource
    private ThreadPoolExecutor executor;

    @Test
    public void seriesNum() throws InterruptedException {
        Set<String> idSet = new LinkedHashSet<>();
        List<String> idList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            executor.execute(() -> {
                for (int j = 0; j < 100; j++) {
                    String threadName = Thread.currentThread().getName();
                    String id = IdGenerator.seriesNum();
                    System.out.println(threadName + " " + id);
                    idSet.add(id);
                    idList.add(id);
                }
            });
        }
        executor.awaitTermination(300, TimeUnit.MILLISECONDS);
        System.out.println("list.size = " + idList.size());
        System.out.println("set.size = " + idSet.size());
    }
}
package com.wang.boot.schedulejob.job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2021/7/7 20:14
 */
@Slf4j
public class BaseJob extends QuartzJobBean {

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("定时任务开始……");
        final JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        final String hostName = (String) jobDataMap.get("hostName");
        final String name = (String) jobDataMap.get("name");
        final String age = (String) jobDataMap.get("age");
        log.info("BaseJob Data: hostName={}, name={}, age={}", hostName, name, age);
        TimeUnit.SECONDS.sleep(3);
        log.info("定时任务执行结束！");
    }
}

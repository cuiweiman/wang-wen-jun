package com.wang.boot.schedulejob;

import com.wang.boot.schedulejob.job.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * @description: 执行定时任务
 * @author: wei·man cui
 * @date: 2021/7/7 17:38
 */
@Slf4j
@Component
@Order(value = Integer.MIN_VALUE)
public class ProcessExecutor implements CommandLineRunner {

    private final SchedulerFactoryBean schedulerFactoryBean;

    public ProcessExecutor(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }


    @Override
    public void run(String... args) throws Exception {
        final Scheduler scheduler = schedulerFactoryBean.getScheduler();

        // 任务对象，以及参数
        final JobBuilder jobBuilder =
                JobBuilder.newJob(BaseJob.class)
                        .withIdentity(BaseJob.class + "job", BaseJob.class + "group")
                        .usingJobData("hostName", InetAddress.getLocalHost().getHostName());

        jobBuilder.usingJobData("name", "Jack");
        jobBuilder.usingJobData("age", "24");
        final JobDetail jobDetail = jobBuilder.build();


        // 定时
        final CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("1 * * * * ?");

        final CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(BaseJob.class + "trigger", BaseJob.class + "triggerGroup")
                .startNow()
                .withSchedule(cronScheduleBuilder)
                .build();

        try {
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (SchedulerException ex) {
            log.error("add process job to scheduler failed.", ex);
            throw new RuntimeException("add process job to scheduler failed.");
        }

    }


}

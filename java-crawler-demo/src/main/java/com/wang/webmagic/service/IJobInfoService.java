package com.wang.webmagic.service;

import com.wang.webmagic.entity.JobInfo;

import java.util.List;

/**
 * @description:
 * @date: 2020/11/15 19:55
 * @author: wei·man cui
 */
public interface IJobInfoService {

    void save(JobInfo jobInfo);

    List<JobInfo> findJobInfo(JobInfo jobInfo);

}

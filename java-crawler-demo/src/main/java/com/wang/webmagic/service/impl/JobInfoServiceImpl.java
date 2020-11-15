package com.wang.webmagic.service.impl;

import com.wang.webmagic.dao.JobInfoDao;
import com.wang.webmagic.entity.JobInfo;
import com.wang.webmagic.service.IJobInfoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @description:
 * @date: 2020/11/15 19:55
 * @author: wei·man cui
 */
@Service
@AllArgsConstructor
public class JobInfoServiceImpl implements IJobInfoService {

    private JobInfoDao jobInfoDao;

    @Override
    public void save(JobInfo jobInfo) {
        // 判断数据库中是否存在数据
        JobInfo param = new JobInfo();
        param.setUrl(jobInfo.getUrl());
        param.setTime(jobInfo.getTime());
        List<JobInfo> list = this.findJobInfo(param);
        // 若已存在就更新
        if (CollectionUtils.isEmpty(list)) {
            this.jobInfoDao.saveAndFlush(jobInfo);
        }
    }

    @Override
    public List<JobInfo> findJobInfo(JobInfo jobInfo) {
        Example<JobInfo> example = Example.of(jobInfo);
        return this.jobInfoDao.findAll(example);
    }
}

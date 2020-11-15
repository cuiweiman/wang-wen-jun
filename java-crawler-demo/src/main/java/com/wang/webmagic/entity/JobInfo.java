package com.wang.webmagic.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @description: 爬取Boss直聘工作信息
 * @date: 2020/11/15 17:24
 * @author: wei·man cui
 */
@Data
@Entity
public class JobInfo {

    @Id
    private Long id;

    private String companyName;

    private String companyAddr;

    private String companyInfo;

    private String jobName;

    private String jobAddr;

    private String jobInfo;

    private String salaryMin;

    private String salaryMax;

    private String url;

    private String time;

}

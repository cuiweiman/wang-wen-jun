package com.wang.tsdb.controller;

import com.baidubce.services.tsdb.TsdbClient;
import com.baidubce.services.tsdb.model.Filters;
import com.baidubce.services.tsdb.model.Group;
import com.baidubce.services.tsdb.model.GroupInfo;
import com.baidubce.services.tsdb.model.Query;
import com.baidubce.services.tsdb.model.QueryDatapointsResponse;
import com.baidubce.services.tsdb.model.Result;
import com.wang.tsdb.domain.WindDomain;
import com.wang.tsdb.utils.OpenCsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: tsdb 数据库 导入导出
 * @Author: cuiweiman
 * @Since: 2021/6/29 下午2:10
 */
@Slf4j
@RestController
@RequestMapping("/files")
public class TsdbController {

    private TsdbClient tsdbClient;

    public TsdbController(TsdbClient tsdbClient) {
        this.tsdbClient = tsdbClient;
    }

    @RequestMapping("/importCsv")
    public boolean importCsv(MultipartFile file) {
        final List<WindDomain> windDomainList = OpenCsvUtil.readFromCsv(file, WindDomain.class);
        windDomainList.forEach(System.out::println);
        return true;
    }

    @RequestMapping("/export")
    public boolean export(HttpServletResponse response) {
        String metric = "wind";
        String field = "speed";
        String field2 = "direction";
        String field3 = "value";
        List<String> fields = Lists.newArrayList("time", field, field2, field3);

        String fileName = "wind-speed-direction-value.csv";
        List<String> headerList = fields;
        List<String[]> lineDataList = new ArrayList<>();
        List<WindDomain> windDomainList = new ArrayList<>();


        Query query = new Query()
                .withMetric(metric)
                // 设置查询的域名，不设置表示查询默认域
                .withFields(fields)
                // 创建Filters对象，设置相对的开始时间，这里设置为2小时前
                .withFilters(new Filters().withRelativeStart("1 week ago")
                        .withRelativeEnd("1 second ago"));

        // 查询数据
        QueryDatapointsResponse queryResponse = tsdbClient.queryDatapoints(Lists.newArrayList(query));
        // 遍历数据
        for (Result result : queryResponse.getResults()) {
            for (Group group : result.getGroups()) {
                for (GroupInfo groupInfo : group.getGroupInfos()) {
                    log.info("[GroupInfo] groupName = {}, ", groupInfo.getName());
                }
                try {
                    for (Group.TimeAndValue timeAndValue : group.getTimeAndValueList()) {
                        final long time = timeAndValue.getTime();
                        String[] lineData = new String[headerList.size()];
                        WindDomain windDomain = new WindDomain();
                        windDomain.setTimeStamp(time);
                        windDomain.setSpeed(timeAndValue.getDoubleValue(1));
                        windDomain.setDirection(timeAndValue.getDoubleValue(2));
                        windDomain.setValue(timeAndValue.getStringValue(3));
                        windDomainList.add(windDomain);

                        lineData[0] = String.valueOf(time);
                        for (int i = 1; i < fields.size(); i++) {
                            if (!timeAndValue.isNull(i)) {
                                if (timeAndValue.isDouble(i)) {
                                    lineData[i] = String.valueOf(timeAndValue.getDoubleValue(i));
                                } else if (timeAndValue.isLong(i)) {
                                    lineData[i] = String.valueOf(timeAndValue.getLongValue(i));
                                } else {
                                    lineData[i] = timeAndValue.getStringValue(i);
                                }
                            }
                        }
                        lineDataList.add(lineData);
                    }
                } catch (IOException e) {
                    log.error("[Group#getTimeAndValue] error: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
        // return CsvUtil.exportToCsv(response, fileName, headerList, lineDataList);
        return OpenCsvUtil.exportToCsv(response, fileName, headerList, lineDataList);
        // return OpenCsvUtil.exportBeanToCsv(response, fileName, windDomainList);
    }


}

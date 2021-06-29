package com.wang.tsdb.utils;

import com.baidubce.services.tsdb.TsdbClient;
import com.baidubce.services.tsdb.model.Filters;
import com.baidubce.services.tsdb.model.Group;
import com.baidubce.services.tsdb.model.GroupInfo;
import com.baidubce.services.tsdb.model.Query;
import com.baidubce.services.tsdb.model.QueryDatapointsResponse;
import com.baidubce.services.tsdb.model.Result;
import com.wang.tsdb.TsdbApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 测试 CSV 文件的导入导出
 * @Author: cuiweiman
 * @Since: 2021/6/29 上午11:46
 */
@Slf4j
public class CsvUtilTest extends TsdbApplicationTest {

    @Autowired
    private TsdbClient tsdbClient;


    /**
     * 导出到 CSV 文件
     */
    @Test
    public void dataExportCsv() {
        String metric = "wind";
        String field = "speed";
        String field2 = "direction";
        String field3 = "value";
        List<String> fields = Lists.newArrayList("time", field, field2, field3);

        String fileName = "wind-speed-direction-value.csv";
        List<String> headerList = fields;
        List<Object[]> lineDataList = new ArrayList<>();

        Query query = new Query()
                .withMetric(metric)
                // 设置查询的域名，不设置表示查询默认域
                .withFields(fields)
                // 创建Filters对象，设置相对的开始时间，这里设置为2小时前
                .withFilters(new Filters().withRelativeStart("1 week ago")
                        .withRelativeEnd("1 second ago"));

        // 查询数据
        QueryDatapointsResponse response = tsdbClient.queryDatapoints(Lists.newArrayList(query));
        // 遍历数据
        for (Result result : response.getResults()) {
            log.info("===================== Result =====================");
            for (Group group : result.getGroups()) {
                log.info("===================== Group =====================");
                for (GroupInfo groupInfo : group.getGroupInfos()) {
                    log.info("[GroupInfo] groupName = {}, ", groupInfo.getName());
                }
                try {
                    for (Group.TimeAndValue timeAndValue : group.getTimeAndValueList()) {
                        final long time = timeAndValue.getTime();
                        Object[] lineData = new Object[headerList.size()];
                        lineData[0] = time;
                        for (int i = 1; i < fields.size(); i++) {
                            if (!timeAndValue.isNull(i)) {
                                if (timeAndValue.isDouble(i)) {
                                    lineData[i] = timeAndValue.getDoubleValue(i);
                                } else if (timeAndValue.isLong(i)) {
                                    lineData[i] = timeAndValue.getLongValue(i);
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
        final boolean export = CsvUtil.exportToCsv(fileName, headerList, lineDataList);
        Assert.assertTrue(export);
    }


}

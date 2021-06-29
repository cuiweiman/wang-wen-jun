package com.wang.tsdb.client;

import com.baidubce.services.tsdb.TsdbClient;
import com.baidubce.services.tsdb.TsdbConstants;
import com.baidubce.services.tsdb.model.Aggregator;
import com.baidubce.services.tsdb.model.Datapoint;
import com.baidubce.services.tsdb.model.Filters;
import com.baidubce.services.tsdb.model.GetFieldsResponse;
import com.baidubce.services.tsdb.model.GetMetricsResponse;
import com.baidubce.services.tsdb.model.GetTagsResponse;
import com.baidubce.services.tsdb.model.Group;
import com.baidubce.services.tsdb.model.GroupInfo;
import com.baidubce.services.tsdb.model.Query;
import com.baidubce.services.tsdb.model.QueryDatapointsResponse;
import com.baidubce.services.tsdb.model.Result;
import com.baidubce.services.tsdb.model.WriteDatapointsResponse;
import com.wang.tsdb.TsdbApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: tsdb client 测试类
 * @Author: cuiweiman
 * @Since: 2021/6/28 下午4:22
 */
@Slf4j
public class TsdbClientTest extends TsdbApplicationTest {

    @Autowired
    private TsdbClient tsdbClient;

    /**
     * 写入 单域/单field 数据点
     * 写入 多个 field 数据点：应注意 时间点 应一直
     */
    @Test
    public void writeSingleFieldTest() {
        // metric
        String metric = "wind";
        String tagKey = "city";
        String tagValue = "ShangHai";
        long timeStamp = System.currentTimeMillis();

        // 创建数据点
        List<Datapoint> windList = new ArrayList<>();
        Datapoint windDirectionPoint = new Datapoint()
                .withMetric(metric)
                // 设置 field 数据点域，若不设置，则为默认的 value
                .withField("direction")
                .addTag(tagKey, tagValue)
                .addDoubleValue(timeStamp, 0.1)
                .addDoubleValue(timeStamp + 1, 15.2);

        Datapoint windSpeedPoint = new Datapoint()
                .withMetric(metric)
                // 设置 field 数据点域，若不设置，则为默认的 value
                .withField("speed")
                .addTag(tagKey, tagValue)
                .addDoubleValue(timeStamp, 15.6)
                .addDoubleValue(timeStamp + 1, 58.9);
        windList.add(windDirectionPoint);
        windList.add(windSpeedPoint);
        WriteDatapointsResponse windResponse = tsdbClient.writeDatapoints(Arrays.asList(windDirectionPoint, windSpeedPoint));
        log.info(windResponse.toString());
        Assert.assertNotNull(windResponse);

        // 其他 metric
        Datapoint tempDataPoint = new Datapoint()
                .withMetric("temperature")
                // 数据点域 没设置，默认为 value
                .addTag(tagKey, tagValue)
                .addLongValue(timeStamp, 10L)
                .addLongValue(timeStamp + 1, 15L);
        WriteDatapointsResponse tempResponse = tsdbClient.writeDatapoints(Arrays.asList(tempDataPoint));
        log.info(tempResponse.toString());
        Assert.assertNotNull(tempResponse);

        // 其他 metric
        Datapoint humidityDataPoint = new Datapoint()
                .withMetric("humidity")
                // 数据点域 没设置，默认为 value
                .addTag(tagKey, tagValue)
                .addDoubleValue(timeStamp, 58)
                .addDoubleValue(timeStamp + 2, 86);
        WriteDatapointsResponse humidityResponse = tsdbClient.writeDatapoints(Arrays.asList(humidityDataPoint));
        log.info(humidityResponse.toString());
        Assert.assertNotNull(humidityResponse);
    }

    /**
     * 写入 多域/多field 数据点
     */
    @Test
    public void writeMultipleFieldTest() {

        String metric = "wind";
        String tagKey = "city";
        String tagValue = "ShangHai";
        // 风向
        String field1 = "direction";
        // 风速
        String field2 = "speed";
        long TIME = System.currentTimeMillis();

        // 添加FIELD_1的数据点
        Datapoint datapoint1 = new Datapoint()
                .withMetric(metric)
                .withField(field1)
                .addTag(tagKey, tagValue)
                .addDoubleValue(TIME, 0.1);

        // 添加FIELD_2的数据点
        Datapoint datapoint2 = new Datapoint()
                .withMetric(metric)
                .withField(field2)
                .addTag(tagKey, tagValue)
                .addLongValue(TIME, 10L);
        WriteDatapointsResponse response = tsdbClient.writeDatapoints(Arrays.asList(datapoint1, datapoint2));
        log.info(response.toString());
        Assert.assertNotNull(response);
    }

    /**
     * 查询 度量：Metric
     */
    @Test
    public void queryMetric() {
        // 获取Metric
        GetMetricsResponse response = tsdbClient.getMetrics();
        // 打印结果
        for (String metric : response.getMetrics()) {
            System.out.println(metric);
        }
    }

    /**
     * 查询 域：Field
     * 包括 value 列
     */
    @Test
    public void queryFields() {
        String METRIC = "wind";
        log.info("===================== FIELDS =====================");
        // 获取Field
        GetFieldsResponse fieldResponse = tsdbClient.getFields(METRIC);
        // 打印结果
        for (Map.Entry<String, GetFieldsResponse.FieldInfo> entry : fieldResponse.getFields().entrySet()) {
            log.info("key = {}, value = {}", entry.getKey(), entry.getValue().toString());
        }
        log.info("===================== TAGS =====================");
        // 获取 Tags
        GetTagsResponse tagsResponse = tsdbClient.getTags(METRIC);
        // 打印结果
        for (Map.Entry<String, List<String>> entry : tagsResponse.getTags().entrySet()) {
            log.info("key = {}, value = {}", entry.getKey(), entry.getValue().toString());
        }
    }


    /**
     * 查询数据点
     * 根据 单域 单Field 和 相对时间
     * value/speed/direction
     */
    @Test
    public void queryDataPointBySingleFieldAndRelativeTime() {
        String metric = "wind";
        String field = "speed";
        List<Query> queries = Arrays.asList(new Query()
                .withMetric(metric)
                // 设置查询的域名，不设置表示查询默认域
                .withField(field)
                // // 创建Filters对象，设置相对的开始时间，这里设置为2小时前
                .withFilters(new Filters().withRelativeStart("120 minutes ago"))
                // 设置返回数据点数目限制
                .withLimit(100)
                // 创建Aggregator对象，1s 内的数据 求和
                .addAggregator(new Aggregator()
                        // 设置聚合函数为Sum
                        .withName(TsdbConstants.AGGREGATOR_NAME_SUM)
                        // 设置采样
                        .withSampling("1 second")
                )
        );

        // 查询数据
        QueryDatapointsResponse response = tsdbClient.queryDatapoints(queries);
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
                        if (timeAndValue.isDouble()) {
                            log.info("timestamp = {} , value = {}", timeAndValue.getTime(), timeAndValue.getDoubleValue());
                        } else if (timeAndValue.isLong()) {
                            log.info("timestamp = {} , value = {}", timeAndValue.getTime(), timeAndValue.getLongValue());
                        } else {
                            log.info("timestamp = {} , value = {}", timeAndValue.getTime(), timeAndValue.getStringValue());
                        }
                    }
                } catch (IOException e) {
                    log.error("[Group#getTimeAndValue] error: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * 分页 查询数据点
     * marker + limit
     */
    @Test
    public void queryDataPointByPages() {
        String metric = "wind";
        String field = "speed";
        Query query = new Query()
                .withMetric(metric)
                // 设置查询的域名，不设置表示查询默认域
                .withField(field)
                // 创建Filters对象，设置相对的开始时间，这里设置为2小时前
                .withFilters(new Filters().withRelativeStart("1 week ago")
                        .withRelativeEnd("1 second ago"));

        while (true) {
            // 查询数据
            QueryDatapointsResponse response = tsdbClient.queryDatapoints(Arrays.asList(query));
            // 遍历数据
            Result result = response.getResults().get(0);

            log.info("===================== Result =====================");
            for (Group group : result.getGroups()) {
                log.info("===================== Group =====================");
                for (GroupInfo groupInfo : group.getGroupInfos()) {
                    log.info("[GroupInfo] groupName = {}, ", groupInfo.getName());
                }
                try {
                    for (Group.TimeAndValue timeAndValue : group.getTimeAndValueList()) {
                        if (timeAndValue.isDouble()) {
                            log.info("timestamp = {} , value = {}", timeAndValue.getTime(), timeAndValue.getDoubleValue());
                        } else if (timeAndValue.isLong()) {
                            log.info("timestamp = {} , value = {}", timeAndValue.getTime(), timeAndValue.getLongValue());
                        } else {
                            log.info("timestamp = {} , value = {}", timeAndValue.getTime(), timeAndValue.getStringValue());
                        }
                    }
                } catch (IOException e) {
                    log.error("[Group#getTimeAndValue] error: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            if (!result.isTruncated()) {
                break;
            }
            // 设置下一页数据的marker
            query.setMarker(result.getNextMarker());
        }
    }


    /**
     * 分页 查询数据点
     * offset + limit
     */
    @Test
    public void queryDataPointByPagesLimitOffset() {
        String metric = "wind";
        String field = "speed";
        String field2 = "direction";
        Query query = new Query()
                .withMetric(metric)
                // 设置查询的域名，不设置表示查询默认域
                .withFields(Arrays.asList(field, field2))
                // 创建Filters对象，设置相对的开始时间，这里设置为2小时前
                .withFilters(new Filters().withRelativeStart("1 week ago")
                        .withRelativeEnd("1 second ago"))
                .withLimit(3)
                .withOffset(0);

        // 查询数据
        QueryDatapointsResponse response = tsdbClient.queryDatapoints(Arrays.asList(query));
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
                        for (int i = 0; i < 2; i++) {
                            log.info("====================  {} ====================", i);
                            if (!timeAndValue.isNull(i)) {
                                if (timeAndValue.isDouble(i)) {
                                    log.info("timestamp = {} , value = {}", timeAndValue.getTime(), timeAndValue.getDoubleValue(i));
                                } else if (timeAndValue.isLong(i)) {
                                    log.info("timestamp = {} , value = {}", timeAndValue.getTime(), timeAndValue.getLongValue(i));
                                } else {
                                    log.info("timestamp = {} , value = {}", timeAndValue.getTime(), timeAndValue.getStringValue(i));
                                }
                            }
                        }

                    }
                } catch (IOException e) {
                    log.error("[Group#getTimeAndValue] error: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
    }


}











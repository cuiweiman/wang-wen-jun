package com.wang.es.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wang.es.entity.JdGoods;
import com.wang.es.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @description:
 * @date: 2020/11/27 0:01
 * @author: wei·man cui
 */
@Service
public class JdGoodServiceImpl {

    @Resource
    private RestHighLevelClient esClient;

    @Resource
    private HtmlParseUtil parseUtil;

    private static final String INDEX_NAME = "jd_index";

    /**
     * 解析数据 放入 es 索引中
     *
     * @param keyWords 关键字
     * @return 结果
     */
    public Boolean parseContent(String keyWords) throws IOException {
        List<JdGoods> jdGoods = parseUtil.parseJdGood(keyWords);
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(2));
        IntStream.range(0, jdGoods.size()).forEach(i -> bulkRequest.add(
                new IndexRequest(INDEX_NAME).id((i + 1) + "")
                        .source(JSONObject.toJSONString(jdGoods.get(i)), XContentType.JSON)));
        BulkResponse response = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !response.hasFailures();
    }

    public List<JdGoods> searchPage(String keyWord, int pageNo, int pageSize) throws IOException {
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder builder = new SearchSourceBuilder();

        // 精确匹配
        TermQueryBuilder termQuery = QueryBuilders.termQuery("name", keyWord);
        builder.query(termQuery);

        // 设置超时，以及分页
        builder.timeout(TimeValue.timeValueSeconds(60));
        builder.from(pageNo);
        builder.size(pageSize);

        // 执行搜索
        searchRequest.source(builder);
        SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
        List<JdGoods> list = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            JdGoods goods = JSONObject.parseObject(hit.getSourceAsString(), JdGoods.class);
            list.add(goods);
        }
        return list;
    }

}

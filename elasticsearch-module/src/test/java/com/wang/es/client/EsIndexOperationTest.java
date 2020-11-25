package com.wang.es.client;

import com.wang.es.EsApplicationTest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @description: ES RestHighLevelClient 操作索引 API 测试
 * @date: 2020/11/25 21:01
 * @author: wei·man cui
 */
public class EsIndexOperationTest extends EsApplicationTest {

    @Resource
    private RestHighLevelClient client;

    private final String INDEX_NAME = "user_index";

    /**
     * 创建索引
     *
     * @throws IOException 异常
     */
    @Test
    public void testCreateIndex() throws IOException {
        // 创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
        // 客户端执行请求，获得响应
        CreateIndexResponse indexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse);
    }

    /**
     * 查询索引是否存在
     *
     * @throws IOException 异常
     */
    @Test
    public void testGetIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest(INDEX_NAME);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(INDEX_NAME + " 索引是否存在：" + exists);
    }


    /**
     * 删除索引
     *
     * @throws IOException 异常
     */
    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(INDEX_NAME);
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(INDEX_NAME + " 是否删除成功：" + response.isAcknowledged());
    }

}

package com.wang.es.client;

import com.alibaba.fastjson.JSONObject;
import com.wang.es.EsApplicationTest;
import com.wang.es.entity.User;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @description: ES RestHighLevelClient 操作文档 API 测试
 * @date: 2020/11/25 23:01
 * @author: wei·man cui
 */
public class EsDocOperationTest extends EsApplicationTest {

    @Resource
    private RestHighLevelClient client;

    private final String INDEX_NAME = "user_index";

    /**
     * 添加文档：
     * 规则： PUT /INDEX-NAME/_doc/1
     *
     * @throws IOException 异常
     */
    @Test
    public void testAddDoc() throws IOException {
        User user = new User("Jack", 24);
        IndexRequest request = new IndexRequest(INDEX_NAME);
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        request.source(JSONObject.toJSONString(user), XContentType.JSON);

        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    /**
     * 查看 指定的 ID文档 是否存在：
     * get /INDEX-NAME/_doc/1
     *
     * @throws IOException 异常
     */
    @Test
    public void testExistDoc() throws IOException {
        GetRequest request = new GetRequest(INDEX_NAME, "1");
        // 返回信息 不获取 _doc 的 上下文
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }


    /**
     * 获取 指定ID 文档
     * get /INDEX-NAME/_doc/1
     *
     * @throws IOException 异常
     */
    @Test
    public void testGetDoc() throws IOException {
        GetRequest request = new GetRequest(INDEX_NAME, "1");
        GetResponse doc = client.get(request, RequestOptions.DEFAULT);
        System.out.println(doc.getSourceAsString());
        System.out.println(JSONObject.toJSONString(doc));
    }

    /**
     * 更新 指定ID 文档
     *
     * @throws IOException 异常
     */
    @Test
    public void testUpdateDoc() throws IOException {
        UpdateRequest request = new UpdateRequest(INDEX_NAME, "1");
        request.timeout(TimeValue.timeValueSeconds(1));
        User jack = new User();
        jack.setAge(22);
        request.doc(JSONObject.toJSONString(jack), XContentType.JSON);

        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
        System.out.println(JSONObject.toJSONString(response));
    }

    /**
     * 删除 指定ID 文档
     *
     * @throws IOException 异常
     */
    @Test
    public void testDeleteDoc() throws IOException {
        DeleteRequest request = new DeleteRequest(INDEX_NAME, "1");
        request.timeout(TimeValue.timeValueSeconds(1));

        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
        System.out.println(JSONObject.toJSONString(response));
    }


    /**
     * 批量 插入 文档
     *
     * @throws IOException 异常
     */
    @Test
    public void testBulkAddDoc() throws IOException {
        List<User> userList = Arrays.asList(
                new User("钱一", 16),
                new User("吴二", 65),
                new User("张三", 13),
                new User("李四", 38),
                new User("王五", 29)
        );

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(1));
        // 构造 多个 IndexRequest，封装到 BulkRequest 中
        IntStream.range(0, userList.size()).forEach(i -> bulkRequest.add(
                new IndexRequest(INDEX_NAME).id(++i + "").source(JSONObject.toJSONString(userList.get(i - 1)), XContentType.JSON)
        ));
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.hasFailures());
        System.out.println(JSONObject.toJSONString(bulk));
    }


    /**
     * 批量 删除 文档
     *
     * @throws IOException 异常
     */
    @Test
    public void testBulkDeleteDoc() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(1));
        // 构造 多个 IndexRequest，封装到 BulkRequest 中
        List<String> idList = Arrays.asList("1", "2", "3", "4", "5");
        bulkRequest.add(idList.stream().map(i -> new DeleteRequest(INDEX_NAME, i)).toArray(DeleteRequest[]::new));

        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.hasFailures());
        System.out.println(JSONObject.toJSONString(bulk));
    }


    /**
     * 搜索
     *
     * @throws IOException 异常
     */
    @Test
    public void testSearchDoc() throws IOException {
        // 精确 匹配 姓名
        TermQueryBuilder builder = QueryBuilders.termQuery("name", "吴二");

        // 构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(builder);
        sourceBuilder.timeout(TimeValue.timeValueSeconds(60));
        /*sourceBuilder.from(1);
        sourceBuilder.size(2);*/

        // 创建 搜索请求
        SearchRequest request = new SearchRequest(INDEX_NAME);
        request.source(sourceBuilder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSONString(response.getHits()));
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }


}

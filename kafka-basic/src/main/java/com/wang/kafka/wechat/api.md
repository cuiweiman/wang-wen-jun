[TOC]

```bash
# 查询模板信息
curl -XGET http://localhost:8080/v1/template

# 查询模板统计结果
curl -XGET http://localhost:8080/v1/template/result

# 传入调查问卷结果
curl -XPOST -H "Content-Type:application/json; charset=UTF-8" http://localhost:8080/v1/template/report -d \
'{
 	"templateId": "001",
 	"result": [{
 			"questionId": "1",
 			"question": "今天几号",
 			"answer": "A"
 		},
 		{
 			"questionId": "2",
 			"question": "你喜爱的颜色",
 			"answer": "B"
 		}
 	]
 }'

netstat -ano | findstr 443
```

### 问卷调查：调查题目查询
- 接口 URL: /v1/template
- 接口请求方式: GET
- 请求参数: 无
- 响应结果：

     ```json
     {
        "requestId": "",
        "result": {
           "templateId": "001",
           "template": [{
                 "questionId": "1",
                 "question": "今天几号",
                 "answer": "",
                 "options": [{
                       "label": "1 号",
                       "value": "A"
                    },
                    {
                       "label": "2 号",
                       "value": "B"
                    },
                    {
                       "label": "3 号",
                       "value": "C"
                    },
                    {
                       "label": "4 号",
                       "value": "D"
                    }
                 ]
              },
              {
                 "questionId": "2",
                 "question": "你喜爱的颜色",
                 "answer": "",
                 "options": [{
                       "label": "红色",
                       "value": "A"
                    },
                    {
                       "label": "黄色",
                       "value": "B"
                    },
                    {
                       "label": "绿色",
                       "value": "C"
                    },
                    {
                       "label": "紫色",
                       "value": "D"
                    }
                 ]
              }
           ]
        }
     }
     ```

### 问卷调查：用户填写结果上传
- 接口 URL: /v1/template/report
- 接口请求方式: POST
- 请求参数:
   ```json
   {
      "templateId": "001",
      "result": [{
         "questionId": "1",
         "question": "今天几号",
         "answer": "A"
      },
      {
         "questionId": "2",
         "question": "你喜爱的颜色",
         "answer": "B"
      }]
   }
   ```
- 响应结果：
   ```bash
   {
      "requestId": ""
   }
   ```



### 调查问卷：调查结果统计

- 接口 URL: /v1/template/result
- 接口请求方式: GET
- 请求参数:
   ```json
   {
      "templateId": "001" //非必填
   }
   ```
- 响应结果：
   ```json
   {
      "requestId": "",
      "result": {
         "templateId": "001",
         "totalNumber": "102",
         "statistics": [{
               "questionId": "1",
               "question": "今天几号",
               "answers": [{
                  "label": "A",
                  "value": 10
               }, {
                  "label": "B",
                  "value": 50
               }, {
                  "label": "C",
                  "value": 12
               }, {
                  "label": "D",
                  "value": 17
               }]
            },
            {
               "questionId": "2",
               "question": "你喜爱的颜色",
               "answers": [{
                  "label": "A",
                  "value": 12
               }, {
                  "label": "B",
                  "value": 52
               }, {
                  "label": "C",
                  "value": 17
               }, {
                  "label": "D",
                  "value": 17
               }]
            }
         ]
      }
   }
   ```


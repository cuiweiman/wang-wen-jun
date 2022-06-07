## docker-compose 部署 spring boot 项目
1. Dockerfile 将此项目构建成 镜像 的描述
2. docker-compose.yml 将微服务项目相关组件进行打包、部署、运行的 配置文件
3. 将 jar 包、Dockerfile、docker-compose.yml 放在服务器的 同一个目录下，执行命令：
docker-compose up -d
4. 访问: ip:8080/api/views 
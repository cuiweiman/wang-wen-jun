FROM java:8
MAINTAINER cuiweiman

RUN mkdir -p /usr/local/app/docker-compose-demo

# 复制 jar 包到指定目录
COPY docker-compose-demo.jar /usr/local/app/docker-compose-demo/docker-compose-demo.jar
# 修改文件的访问时间和修改时间为 当前时间
RUN bash -c "touch /usr/local/app/docker-compose-demo/docker-compose-demo.jar"

# 镜像运行为容器后, 执行的命令
# ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /usr/local/app/docker-compose-demo/docker-compose-demo.jar ${0} ${@}"]
ENTRYPOINT ["sh", "-c", "java -jar /usr/local/app/docker-compose-demo/docker-compose-demo.jar ${0} ${@}"]


# 容器启动命令
# docker run --restart=on-failure:3 --name docker-compose-demo \
#   -p:8080:8080 -e "JAVA_OPTS=-Xms1G -Xmx1G -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/docker-compose-demo.hprof \
#   -d docker-compose-demo:1.0
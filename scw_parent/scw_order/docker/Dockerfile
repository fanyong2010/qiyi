# 基于哪个镜像
FROM java:8
# 拷贝文件到容器
COPY scw_order-*.jar /app.jar
# 声明需要暴露的端口
EXPOSE 8888
#配置环境变量，接受外部传递参数
#监听端口
ENV PORT=""
#应用名称
ENV APPLICATION=""
#注册中心地址
ENV EUREKA=""
# 配置容器启动后执行的命令
ENTRYPOINT ["sh","-c","java $PORT $APPLICATION $EUREKA  -jar /app.jar"]
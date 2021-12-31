# Muses

基于`SpringCloudAlibaba`技术栈微服务模板项目

## 模块

|模块|描述|
|:-|:-|
|boot|启动|
|gateway|网关|
|web-parent|Web服务模块|
|rest-parent|Rest服务模块|
|boot-parent|Boot模块|
|service-parent|内部服务模块|
|docs|配置文档|

## 规范

#### 开发日志

* `[+]` 添加功能
* `[-]` 删除功能
* `[*]` 修改功能
* `[!]` 危险操作
* `[#]` 没有完成
* `[~]` 日常任务

#### 符号说明

* `+`：必选
* `*`：可选

## 安全

#### Web服务

`Web`服务使用传统`Session`记录状态，通过`RedisSession`实现共享，需要用户自己通过拦截器或者`Spring Security`实现认证授权。

#### Rest服务

`Rest`服务使用`OAuth2`服务通过网关实现统一认证鉴权

## 配置

配置统一放在`Nacos`配置中心通过`bootstrap.yml`加载：

```
spring:
  application:
    name: acgist
  main:
    allow-bean-definition-overriding: true
  profiles:
    active: dev
  cloud:
    nacos:
      username: nacos
      password: nacos
      discovery:
        namespace: ${spring.profiles.active}
        server-addr: localhost:8848
      config:
        namespace: ${spring.profiles.active}
        file-extension: yml
        refreshable-dataids: redis.yml, dubbo.yml
```

> 配置备份[./docs/nacos_config.zip](./docs/nacos_config.zip)

## 关机

* 服务下线
* `kill -2 pid`

## 系统配置

```
# 系统编号（负数自动生成）：主要用于生成ID，相同服务建议设置不同编号。
system.sn=01
# 系统端口：随机生成
system.port=8080
# 线程初始数量
system.thread.min=2
# 线程最大数量
system.thread.max=10
# 线程最大长度
system.thread.size=1000
# 线程活跃时间：秒
system.thread.live=30
# 系统序列化类型
system.serializer.type=jdk|jackson
# 是否启用拦截器
system.rest.interceptor=true|false
# 慢请求统计时间：毫秒
system.gateway.slow.request.duration=1000
# 静态资源地址
system.static.host=//localhost:8888
# 网关Topic
system.topic.gateway=topic-gateway
```

## Maven配置

```
# JVM参数
system.maven.jvm=
# JVM参数：xms
system.maven.xms=256M
# JVM参数：xmx
system.maven.xmx=512M
# 是否打包
system.maven.unpack=true
# 作者
system.maven.vendor=acgist
# 模块
system.maven.module=com.acgist.muses
# 项目的根目录
system.maven.basedir=${project.basedir}
# 版本
system.maven.version=1.0.0
# 编码
system.maven.encoding=UTF-8
```

## 端口

|模块|端口|
|:-|:-|
|gateway|8888|
|rest-oauth2|9999|
|web|[18000,19000)|
|rest|[19000,20000)|

#### 固定端口

```
# 配置
server.port=8888
# 命令
java -jar appliation.jar --server.port=8888
java -Dserver.port=8888 -jar appliation.jar
java -jar appliation.jar --system.port=8888
java -Dsystem.port=8888 -jar appliation.jar
```

#### 完全随机

```
server.port=0
```

#### 范围随机

```
server.port=${system.port:8080}
```

## 优化位置

/boot-gateway/src/main/java/com/acgist/gateway/service/GatewayChannel.java
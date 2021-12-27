# SpringCloudAlibaba

## 认证鉴权

* Web服务使用传统Session记录状态通过Redis实现共享
* REST服务使用Oauth2服务通过JWT实现网关统一认证鉴权

## 开发日志

* [+] 添加功能
* [-] 删除功能
* [*] 修改功能
* [!] 危险操作
* [#] 没有完成
* [~] 日常任务

## 符号说明

* `+`：必选
* `*`：可选

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

## 配置

配置建议放在Nacos配置中心通过bootstrap.yml加载：

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

> Nacos配置备份[./docs/nacos_config.zip](./docs/nacos_config.zip)

## 关机

* 服务下线
* `kill -2 pid`

## 系统配置

```
# 系统序列号
system.sn=01
# 线程初始数量
system.thread.min=2
# 线程最大数量
system.thread.max=10
# 线程最大长度
system.thread.size=1000
# 线程活跃时间
system.thread.live=30
# 序列化类型：Redis
system.serializer.type=jdk|jackson
# 是否启用拦截器
system.rest.interceptor=true|false
# 慢请求统计时间
system.gateway.slow.request.duration=1000
```

## 优化位置

/boot-gateway/src/main/java/com/acgist/gateway/service/GatewayChannel.java
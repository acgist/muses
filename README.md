# SpringCloudAlibaba

## 认证鉴权

Web服务已经实现用户登陆需要自己实现鉴权
REST服务配合Oauth2服务通过网关实现认证鉴权

## TODO

Oauth2使用redis保存token
Oauth2使用配置中心添加配置
Oauth2使用jks而不是自动生成证书
网关鉴权能否去掉服务调用：使用token保存权限？但是数据太大性能？

## 模块

|模块|描述|
|:-|:-|
|boot|启动模块|
|boot-parent|通用模块|
|gateway|网关模块|
|web-parent|Web服务：网页相关|
|rest-parent|Rest服务：接口相关|
|service-parent|内部服务|
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

* 端点
* 通过MQ广播
* `kill -2 pid`

## 模块

其他模块使用

#### sentinel dashboard

```
<dependency>
	<groupId>com.alibaba.csp</groupId>
	<artifactId>sentinel-transport-simple-http</artifactId>
</dependency>
```

#### dubbo sentinel adapter

```
<dependency>
	<groupId>com.alibaba.csp</groupId>
	<artifactId>sentinel-apache-dubbo-adapter</artifactId>
</dependency>
```

> 通过配置服务入口熔断限流，所以暂时不会使用dubbo服务限流熔断。

#### gateway sentinel adapter

```
<dependency>
	<groupId>com.alibaba.cloud</groupId>
	<artifactId>spring-cloud-alibaba-sentinel-gateway</artifactId>
</dependency>
```

#### actuator

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

#### prometheus

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
	<groupId>io.micrometer</groupId>
	<artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

#### retry

```
<dependency>
	<groupId>org.springframework.retry</groupId>
	<artifactId>spring-retry</artifactId>
</dependency>

@EnableRetry

@Retryable(value = NumberFormatException.class, maxAttempts = 10, backoff = @Backoff(delay = 1000, maxDelay = 5000, multiplier = 2))
public void retry() {
}
```

#### 系统配置

```
# 系统序列号
system.sn=01
# 序列化类型：Redis
system.serializer.type=jdk|jackson
# 是否启用拦截器
system.rest.interceptor=true|false
# 慢请求统计时间
system.gateway.slow.request.duration=1000
```


#### pom.xml

* java
* javax
* 本身依赖
* 外部依赖
* Boot依赖
* Cloud依赖
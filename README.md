# SpringCloudAlibaba

## 认证授权

Web服务和Rest通过网关对外提供服务，Web服务需要自己实现身份认证，Rest服务统一网关进行身份认证，权限鉴定需要都要自己实现。

#### UserService

需要自己实现用户查询服务逻辑

## 模块

|模块|描述|
|:-|:-|
|common|通用模块|
|gateway|网关模块|
|web-parent|Web服务：网页相关|
|rest-parent|Rest服务：接口相关|
|common-parent|通用模块|
|service-parent|内部服务|

## 配置

配置建议放在Nacos配置中心通过bootstrap.yml加载：

```
spring:
  application:
    name: acgist
  main:
    allow-bean-definition-overriding: true
  profiles:
# 通过环境加载：application-dev/application-data/application-redis
    active: dev, data, redis
  cloud:
    nacos:
      username: nacos
      password: nacos
      discovery:
        server-addr: localhost:8848
# 指定文件加载
      config:
        ext-config:
          - data-id: redis.properties
            refresh: true
            group: DEFAULT_GROUP
```

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

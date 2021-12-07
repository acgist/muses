# SpringCloudAlibaba

## 认证授权

Web服务和Rest通过网关对外提供服务，Web服务需要自己实现身份认证，Rest服务统一网关进行身份认证，权限鉴定需要都要自己实现。

#### UserService

需要自己实现用户查询服务逻辑

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
# 序列化类型：Redis
system.serializer.type=jdk|jackson
# 是否启用拦截器
system.gateway.interceptor=true|false
```


#### pom.xml

* java
* javax
* 本身依赖
* 外部依赖
* Boot依赖
* Cloud依赖
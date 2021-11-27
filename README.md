# SpringCloudAlibaba

Web服务和Rest通过网关对外提供服务，网关管理Rest服务权限，Web服务需要自行实现权限验证。

> 根据自己情况保留选取模块

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

## 服务

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

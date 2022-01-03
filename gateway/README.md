# Gateway

网关：通过网关访问内部Www服务

## 跨域

统一处理跨域问题

## 负载均衡

```
lb://服务名称
```

## 认证授权

Rest服务统一使用`rest-oauth2`认证授权

## 模块

#### gateway sentinel adapter

```
<dependency>
	<groupId>com.alibaba.cloud</groupId>
	<artifactId>spring-cloud-alibaba-sentinel-gateway</artifactId>
</dependency>
```

## 注意事项

* 不能配置`issuer-uri`
* Web服务和Rest服务可以分开两个网关

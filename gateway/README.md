# 网关

网关主要负责负载均衡、认证鉴权

> 除此之外其他网关均为系统接口

## 跨域

统一处理跨域问题

## 负载均衡

## 认证授权

Rest服务统一使用`rest-oauth2`认证授权

## 注意

* 不能配置`issuer-uri`
* 可以将Web和Rest分开两个网关
* Web登陆成功访问Rest服务时会导致Session失效

## 模块

#### gateway sentinel adapter

```
<dependency>
	<groupId>com.alibaba.cloud</groupId>
	<artifactId>spring-cloud-alibaba-sentinel-gateway</artifactId>
</dependency>
```

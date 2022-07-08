# 网关

通过网关访问内部Www服务

> 注意：不要引入`spring-boot-starter-web`模块

## 跨域

统一处理跨域问题

## 负载均衡

```
lb://服务名称
```

## 认证授权

#### Web

Web服务需要自己实现认证授权

#### Rest

Rest服务统一使用`rest-oauth2`认证授权

## 注意事项

* 不能配置`issuer-uri`
* Web服务和Rest服务可以分开两个网关

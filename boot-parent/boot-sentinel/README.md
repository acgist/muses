# 熔断降级Boot

提供熔断降级功能

## URL清洗

```
sentinel:
  url-cleaner:
    "[/user]": /user/*
    "[/user/memo]": /user/memo/*
```

## 模块

默认只有网络服务引入，通过流量入口直接限制，如果需要限制`Dubbo`和`Gateway`以及监控功能需要引入相关依赖。

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

#### gateway sentinel adapter

```
<dependency>
	<groupId>com.alibaba.cloud</groupId>
	<artifactId>spring-cloud-alibaba-sentinel-gateway</artifactId>
</dependency>
```

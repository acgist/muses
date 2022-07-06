# 模块Boot

各种自动配置模块

## Www模块继承关系

```
/-boot-www：网络服务
  /-boot-web：网页资源服务
  /-boot-rest：Rest接口服务：前后端分离时提供接口服务
    /-boot-gateway：网关Rest接口服务：外部用户提供接口服务（提供签名、验签、报文保存推送等等功能）
```

## 更多模块

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
	<groupId>io.micrometer</groupId>
	<artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

> 需要配合`actuator`

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

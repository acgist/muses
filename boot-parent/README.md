# Boot-Parent

Boot模块：各种模块自动配置

## boot-dev

开发模块建议不要打包依赖：`<optional>true</optional>`

## Www模块继承

```
/-boot-www：网络服务
  /-boot-web：网页资源服务
  /-boot-rest：Rest接口服务：前后端分离时提供接口服务
    /-boot-gateway：网关Rest接口服务：对外用户提供接口服务
```

## 更多模块

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

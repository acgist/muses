## boot

底层原始依赖配置

## 模块

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
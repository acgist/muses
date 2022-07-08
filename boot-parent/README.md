# 依赖Boot

提供各种依赖自动配置

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

#### jasypt

配置文件加密解密

```
# 依赖
<dependency>
	<groupId>com.github.ulisesbocchio</groupId>
	<artifactId>jasypt-spring-boot-starter</artifactId>
</dependency>
# 注解
@EnableEncryptableProperties
# 配置
jasypt:
  encryptor:
    PoolSize: 2
    Password: ${encrypt.password:123456}
    Algorithm: PBEWITHHMACSHA512ANDAES_256
    ProviderName: SunJCE
    StringOutputType: base64
    IvGeneratorClassName: org.jasypt.iv.RandomIvGenerator
    KeyObtentionIterations: 1000
    SaltGeneratorClassName: org.jasypt.salt.RandomSaltGenerator
# 使用
password: ENC(密文)
# 代码
@Test
public void testEncrypt() {
	final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
	encryptor.setConfig(this.config());
	log.info("加密结果：{}", encryptor.encrypt("123456"));
}

@Test
public void testDecrypt() {
	final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
	encryptor.setConfig(this.config());
	log.info("解密结果：{}", encryptor.decrypt("9UV27ZHozeJYJ8HampKJPWXHV/imBptqghRxjTJQvMhD1VFnmrZ+l43PVtRF3KEW"));
}

private SimpleStringPBEConfig config() {
	final SimpleStringPBEConfig config = new SimpleStringPBEConfig();
	config.setPoolSize("2");
	config.setPassword("123456");
	config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
	config.setProviderName("SunJCE");
	config.setStringOutputType("base64");
	config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
	config.setKeyObtentionIterations("1000");
	config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
	return config;
}
```

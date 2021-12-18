## 认证服务

#### jwt.jks

```
keytool -genkey -alias jwt -keyalg RSA -keysize 1024 -keystore jwt.jks -validity 3650 -keypass acgist -storepass acgist
```

#### 代码优化

>
已经废弃
~~spring-security-oauth2~~
~~spring-cloud-starter-oauth2~~

```
spring-security-oauth2-client
spring-security-oauth2-resource-server
spring-security-oauth2-authorization-server
spring-boot-starter-oauth2-client
spring-boot-starter-oauth2-resource-server
```

#### OIDC


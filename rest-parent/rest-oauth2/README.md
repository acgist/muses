## 认证服务

#### jwk.jks

```
keytool -genkeypair -alias jwk -keyalg RSA -keysize 2048 -keystore jwk.jks -validity 3650 -storetype jks -keypass acgist -storepass acgist
```

#### 注意事项

公钥和客户端ID不能随机生成，否者重启之后Redis数据就会失效。

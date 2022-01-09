# Rest-OAuth2

认证授权Rest服务

## 术语

|术语|描述|
|:-|:-|
|JWT|JSON Web Token|
|JWS|签过名的JWT|
|JWK|JWT签名密钥|

## jwk.jks

```
keytool -genkeypair -alias jwk -keyalg RSA -keysize 2048 -keystore jwk.jks -validity 3650 -storetype jks -keypass acgist -storepass acgist
```

## 注意事项

公钥和客户端ID不能随机生成，否者重启之后Redis数据就会失效。

# 网关Rest接口Boot

提供网关Rest接口服务，请求报文和响应报文推送到消息队列。

## 接口映射

```
gateway:
  mapping:
    - record: false
      gateway: GET:/user/memo
      name: 查询用户备注
      clazz: com.acgist.gateway.request.GetMemoRequest
    - record: true
      gateway: POST:/user/memo
      name: 修改用户备注
      clazz: com.acgist.gateway.request.SetMemoRequest
```

## 安全

* 加密重要请求数据
* 响应数据需要签名

## 请求数据

```
# 代码：所有请求响应信息
GatewaySession.getInstance()
# 参数注解
@GatewayBody <T extend GatewayRequest> request
```

## 注意事项

* 如果需要数据推送建议添加白名单或者验证请求地址：防止请求内部服务导致系统异常（特别是启动端点的情况）

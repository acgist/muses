# Boot-Gateway

网关Rest模块Boot：提供网关Rest接口服务

## 配置

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

* 响应数据需要签名
* 重要请求数据需要加密

## 注意事项

* 如果需要数据推送建议添加白名单或者验证请求地址：防止请求内部服务导致系统异常（特别是启动端点的情况）
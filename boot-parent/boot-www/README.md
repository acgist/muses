# 网络服务Boot

所有通过网络提供服务的模块必须包含此依赖

## Www模块继承关系

```
/-boot-www：网络服务
  /-boot-web：网页资源服务
  /-boot-rest：Rest接口服务
    /-boot-gateway：网关Rest接口服务
```

#### Rest接口服务

前后端分离时提供Rest接口服务

#### 网关Rest接口服务

外部用户提供Rest接口服务，提供签名、验签、报文保存推送等等功能。

## `Controller`返回数据格式

#### Web

没有固定格式

#### Rest

全部返回Message<?>包装数据

## 拦截器

如果拦截器直接使用异常抛出错误，务必判断是否跳转错误页面避免造成死循环。

## 加密解密

系统提供`RsaPasswordEncoder`支持明文和密文两种方式密码验证方式

## Controller

### 分页

支持前缀连表，分页查询参数`ew`，查询条件`${ew.customSqlSegment}`。

```
GET /admin/user?current=1&size=20

{
	"filter" : [
		{"name" : "name", "type" : "like", "value" : "acgist"},
		{"name" : "age", "type" : "in", "value" : [10, 11, 12, 13]},
		{"name" : "sex", "type" : "eq", "value" : 1}
	],
	"sorted" : [
		{"name" : "name", "type" : "ASC"},
		{"name" : "createDate", "type" : "DESC"}
	]
}
```

> 分页参数可以写在`FilterQuery`里面

### 保存更新

```
POST /admin/user

{
	"id" : 1,
	"name" : "acgist"
}
```

> 根据ID判断保存还是更新

### 查询

```
GET /admin/user/{id}
```

### 删除

```
DELETE /admin/user/{id}
```

### 批量删除

```
DELETE /admin/user

[1, 2, 3, 4]
```

## 数据校验

### @Valid

```
(@Valid @RequestBody Dto dto, BindingResult bindingResult) {
	if(bindingResult.hasErrors()) {
		bindingResult.getAllErrors()
	}
}
```

### @Valid + @Validated

全局处理抛出异常

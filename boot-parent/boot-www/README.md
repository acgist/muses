# Boot-Www

Www模块Boot：所有通过网络提供服务的模块必须包含此依赖

## 拦截器

如果拦截器直接使用异常抛出错误，务必判断是否跳转错误页面避免造成死循环。

## 加密解密

登陆需要使用`RSA`加密，需要自己实现`PasswordEncoder`修改密码匹配时先解密然后匹配。

## 认证中心

如果需要使用其他认证中心认证帐号，可以自行提供`PasswordEncoder`进行覆盖。

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

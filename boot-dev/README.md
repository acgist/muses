# Boot-Dev

快速开发模块，提供`lombok`、`mapstruct`，默认`<scope>provided</scope>`直接引入不会打包。

## 代码生成

提供代码生成工具`CodeBuilderTest`

## Controller

### 分页

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

# Service-Parent

内部服务模块：内部Dubbo服务

## API

服务调用应该使用`DTO`进行数据传递不要使用`Entity`进行数据传递

## Model

提供模型数据：VO/DTO/Entity

## 服务使用

引用相应服务模块`API`即可导入服务

> 前端可以直接使用REST进行服务分解，不想分得太细可以忽略这个模块，直接与REST合并一起使用。

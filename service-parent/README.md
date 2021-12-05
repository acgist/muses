## 内部服务

对内使用Dubbo提供服务，不要使用任何Web模块。

#### API

引用`boot-data`使用`<optional>true</optional>`

服务调用应该使用Dto进行数据传递不要使用Entity进行数据传递

> 如果需要使用Entity需要引入`boot-data`

#### 服务使用

引用相应模块API即可导入服务

#### 注意事项

如果需要对外提供接口调用需要提供API
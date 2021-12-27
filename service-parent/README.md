# 内部服务

对内使用Dubbo提供服务，不要使用任何Web模块。

## API

引用`boot-data`使用`<optional>true</optional>`

服务调用应该使用`Dto`进行数据传递不要使用`Entity`进行数据传递

## 服务使用

引用相应服务模块`API`即可导入服务

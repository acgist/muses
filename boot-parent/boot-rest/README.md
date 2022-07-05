# Boot-Rest

Rest模块Boot：提供Rest接口服务

## 当前用户

```
# 代码
UserContext.get()
# 参数注解
@CurrentUser Long id
@CurrentUser(Type.ID) Long id
@CurrentUser String name
@CurrentUser(Type.NAME) String name
@CurrentUser User user
@CurrentUser(Type.USER) User name
```

## 接口文档

```
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-boot-starter</artifactId>
</dependency>
```

> 目前只有`dev`环境有效：`/swagger-ui/index.html`

#### Swagger3

[https://springdoc.org/](https://springdoc.org/)

###### 注解修改

```
@Api → @Tag
@ApiIgnore → @Parameter(hidden = true) or @Operation(hidden = true) or @Hidden
@ApiImplicitParam → @Parameter
@ApiImplicitParams → @Parameters
@ApiModel → @Schema
@ApiModelProperty(hidden = true) → @Schema(accessMode = READ_ONLY)
@ApiModelProperty → @Schema
@ApiOperation(value = "foo", notes = "bar") → @Operation(summary = "foo", description = "bar")
@ApiParam → @Parameter
@ApiResponse(code = 404, message = "foo") → @ApiResponse(responseCode = "404", description = "foo")
```

## 空指针错误

如果启动报空指针错误选择以下任意一种解决方式：

1. 在启动类添加注解：`@EnableWebMvc`
2. 添加配置：`spring.mvc.pathmatch.matching-strategy=ANT_PATH_MATCHER`

## 页面模块

如果不是简单列表页面，建议一个页面使用一个`PageVO`，然后页面组件再使用一个组件`ModuleVO`。
`PageVO`包含多个`ModuleVO`，`PageVO`和`ModuleVO`接口分开，进入页面统一使用`PageVO`接口，刷新某个组件使用`ModuleVO`接口。

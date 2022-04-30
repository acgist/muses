# Boot-Rest

Rest模块Boot：提供Rest接口服务

## 当前用户

```
# 代码
UserContext.get()
# 参数注解
@CurrentUser User user
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
# Muses

基于`SpringCloudAlibaba`技术栈微服务模板项目

## 模块

|模块|描述|
|:-|:-|
|boot|启动|
|gateway|网关|
|web-parent|Web服务模块|
|rest-parent|Rest服务模块|
|boot-parent|Boot模块|
|service-parent|内部服务模块|
|docs|配置文档|

## 规范

#### 开发日志

* `[+]` 添加功能
* `[-]` 删除功能
* `[*]` 修改功能
* `[!]` 危险操作
* `[#]` 没有完成
* `[~]` 日常任务

#### Service规范

* 远程`Service`接口需要使用`I`开头：`IUserService`
* 本地`Service`接口不要使用`I`开头：`UserService`
* 实现使用`Impl`结尾：`UserServiceImpl`
* 不要使用继承`BootService`提供远程服务

#### 符号说明

* `+`：必选
* `*`：可选

## 安全

#### Web服务

`Web`服务使用传统`Session`记录状态，通过`RedisSession`实现共享，需要用户自己通过拦截器或者`Spring Security`实现认证授权。

#### Session失效

如果`Web`登录成功访问没有集成`RedisSession`的项目，那么就会导致`Session`失效。
解决方法设置`session`的`cookie.name`、`cookie.domain`或者`cookie.path`进行区分。

#### Rest服务

`Rest`服务使用`OAuth2`服务通过网关实现统一认证鉴权

#### 测试

为了方便测试后台接口，可以绕过网关直接请求`WWW(Web/Rest)`服务，需要配置`system.rest.ignore-user=true`忽略用户拦截。
如果需要模拟用户登陆需要添加请求头部`current-user`设置登陆用户信息。

## 配置

配置统一放在`Nacos`配置中心通过`bootstrap.yml`加载：

```
spring:
  application:
    name: acgist
  main:
    allow-bean-definition-overriding: true
  profiles:
    active: @profile@
  cloud:
    nacos:
      username: nacos
      password: nacos
      discovery:
        namespace: ${spring.profiles.active}
        server-addr: @nacos@
      config:
        namespace: ${spring.profiles.active}
        file-extension: yml
        refreshable-dataids: redis.yml, dubbo.yml
```

> 配置备份[./docs/nacos_config.zip](./docs/nacos_config.zip)

## 关机

* 服务下线
* `kill -2 pid`

## 系统配置

```
# 系统名称
system.name=muses
# 系统版本
system.version=1.0.0
# 系统编号（负数自动生成）：主要用于生成ID，相同服务建议设置不同编号。
system.sn=-1
# 系统端口：随机生成
system.port=8080
# 线程初始数量
system.thread.min=2
# 线程最大数量
system.thread.max=10
# 线程队列最大长度
system.thread.size=100000
# 线程活跃时间：秒
system.thread.live=30
# 系统序列化类型
system.serializer.type=jdk|jackson
# 是否自动配置MVC：请求拦截器、参数解析器
system.mvc=true|false
# Rest忽略用户拦截
system.rest.ignore-user=true|false
# 慢请求统计时间：毫秒
system.gateway.slow.request.duration=1000
# 静态资源地址
system.static.host=//localhost:8888
# 网关Topic
system.topic.gateway=topic-gateway
# 是否允许下线自动关机
system.shutdown.enable=true
# 自动关机等待时间：秒
system.shutdown.gracefully=30
# 缓存前缀
system.cache.prefix=cache::
```

## Maven配置

```
# 应用启动JVM参数
system.maven.jvm=
# 应用启动JVM参数：xms
system.maven.xms=256M
# 应用启动JVM参数：xmx
system.maven.xmx=512M
# 是否忽略打包
system.maven.unpack=true
# 作者
system.maven.vendor=acgist
# 模块
system.maven.module=com.acgist.muses
# 项目的根目录
system.maven.basedir=${project.basedir}
# 编码
system.maven.encoding=UTF-8
```

## 端口

|模块|端口|
|:-|:-|
|gateway|8888|
|rest-oauth2|9999|
|web|[18000,19000)|
|rest|[19000,20000)|

#### 固定端口

```
# 配置
server.port=8888
# 命令
java -jar appliation.jar --server.port=8888
java -Dserver.port=8888 -jar appliation.jar
java -jar appliation.jar --system.port=8888
java -Dsystem.port=8888 -jar appliation.jar
```

#### 完全随机

```
server.port=0
```

#### 范围随机

```
server.port=${system.port:8080}
```

## 开发

服务实例里面所有业务逻辑都是测试使用，可以根据自己实际情况进行修改删除。

#### 命名规范

|名称|包名|描述|
|:-|:-|:-|
|配置|com.acgist.**.config||
|过滤器|com.acgist.**.filter||
|监听器|com.acgist.**.listener||
|拦截器|com.acgist.**.inteceptor||
|控制器|com.acgist.**.controller||
|微服务|com.acgist.**.api.impl||
|本地服务|com.acgist.**.service.impl|接口`I`开头，实现接口`Impl`结尾，没有接口本地服务不用`Impl`结尾。|
|数据层|com.acgist.**.dao.*|es/neo4j/mapper/repository|
|数据模型|com.acgist.**.model.*|es/neo4j/entity/vo/dto/enum/request/response/exception|

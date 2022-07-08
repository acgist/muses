# Dubbo服务Boot

提供`Dubbo`服务注册调用功能

## 命名规范

* 支付接口模块`web|rest|service-pay-api`提供接口`IPayService`
* 支付服务模块`web|rest|service-pay-impl`提供实现`PayServiceImpl`
* 支付模型模块`web|rest|service-pay-model`提供模型`PayDto`/`PayEntity`/`PayVo`

#### Service命名规范

服务`Service`要以`I`开头，本地`Service`不用，实现均要使用`Impl`结尾。

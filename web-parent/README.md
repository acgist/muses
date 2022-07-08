# Web服务

提供网页资源服务

## 状态

Web服务使用`Redis Session`记录登陆状态

## 认证授权

需要用户自己通过拦截器或者`Spring Security`实现

## 静态资源

静态资源建议使用Nginx直接提供或者使用CDN

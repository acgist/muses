# Boot-Sentinel

熔断降级模块Boot

## URL清洗

```
sentinel:
  url-cleaner:
    "[/user]": /user/*
    "[/user/memo]": /user/memo/*
```

## 注意事项

默认只有网络服务引入，通过流量入口直接限制，如果需要限制`Dubbo`和`Gateway`可以引入相关依赖。
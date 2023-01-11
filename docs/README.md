# 自动打包

通过配置Maven属性`system.maven.basedir`统一设置打包脚本

## 部署

```
# 部署
sh docs/deploy/deployJava.sh dev
# 打包
sh docs/deploy/packageJava.sh dev
# 版本
sh docs/deploy/versionJava.sh 1.0.0
```

## 启动

```
sh startup.sh
```

#### Linux

如果使用Windows打包然后使用Linux运行，脚本需要设置：`:set ff=unix`

## 配置

可以通过`config`外置目录配置覆盖内置配置

#### 加载顺序

```
./custom-config/application.properties
classpath:/custom-config/application.properties
./config/application.properties
./application.properties
classpath:/config/application.properties
classpath:/application.properties
```

> 优先级由高到低

#### 配置建议

建议统一使用配置中心

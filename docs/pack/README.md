# 自动打包

通过配置Maven属性`acgist.basedir`统一设置打包脚本

## 启动

```
./bin/startup.sh
./bin/startup.bat
```

#### Linux

Linux脚本需要设置：`:set ff=unix`

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
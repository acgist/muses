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

可以通过`config`目录配置覆盖内置配置：

```
./config/application.properties
```

#### 加载顺序

```
./config/application.properties
./application.properties
classpath:config/application.properties
classpath:application.properties
```

# 自动打包

通过配置Maven属性`system.maven.basedir`统一设置打包脚本

## 部署

#### 单个应用

```
# 打包安装不用启动
sh deployApp.sh
# 打包安装同时启动
sh deployStartup.sh
```

#### 多个应用

修改单个应用脚本名称，然后编辑`deployAll.sh`进行启动。

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

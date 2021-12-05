## 使用

#### 启动

```
./bin/startup.bat
```

> 必须严格按照位置执行

#### 配置

可以通过config目录创建配置覆盖配置：

```
./config/application.properties
```

###### 加载顺序

```
./config/application.properties
./application.properties
classpath:config/application.properties
classpath:application.properties
```

> 建议通过自动打包配置
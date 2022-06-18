# 日志记录

使用`canal`监听`MySQL`数据变化并记录，同时实现数据对比和日志输出。

## Canal

同步变化数据

> [https://github.com/alibaba/canal](https://github.com/alibaba/canal)

#### MySQL配置

[https://github.com/alibaba/canal/wiki/QuickStart](https://github.com/alibaba/canal/wiki/QuickStart)

```
# 配置
[mysqld]
# 开启 binlog
log-bin=binlog
# 选择 ROW 模式
binlog-format=ROW
# 配置 ServerID
server_id=1

# 查询
show variables like '%log_bin%'

# 授权
CREATE USER canal IDENTIFIED BY 'canal';
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
# 如果权限问题登陆不了使用下面授权语句
-- GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%';
FLUSH PRIVILEGES;
```

#### Canal配置

```
# 配置数据连接（instance.properties）：example -> muses
canal.instance.mysql.slaveId=2
canal.instance.master.address=127.0.0.1:3306
canal.instance.dbUsername=canal
canal.instance.dbPassword=canal
# 过滤表名
canal.instance.filter.regex=muses\\.t_.*
canal.mq.topic=log-topic

# 配置Kafka（instance.properties）
canal.serverMode=kafka
kafka.bootstrap.servers=127.0.0.1:9092
```

## Kafka

传输变化数据

```
# 创建Topic
kafka-topics.sh --zookeeper zookeeper:2181 --topic topic-log --create --partitions 3 --replication-factor 3

# 配置Kafka
spring:
  cloud:
    stream:
      binders:
        log:
          type: kafka
      default-binder: log
      function:
        definition: logRecord
      bindings:
        logRecord-in-0:
          group: ${spring.application.name}
          binder: log
          destination: ${system.topic.log:topic-log}
#         content-type: text/plain
```

## ElasticSearch

记录变化数据

```
```

## 模板

差异模板输出

```
```

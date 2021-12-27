# 数据

## 支持数据源

```
ES *
MySQL
Neo4j *
```

> 默认数据库是`MySQL`

## 分库分表

#### Maven

```
<dependency>
	<groupId>org.apache.shardingsphere</groupId>
	<artifactId>sharding-jdbc-spring-boot-starter</artifactId>
</dependency>
```

#### 配置

```
spring:
  jpa:
    database: MYSQL
    show-sql: true
    open-in-view: false
    database-platform: org.hibernate.dialect.MySQL57Dialect
    hibernate:
      ddl-auto: none
      jdbc:
        batch_size: 10
        fetch_size: 10
    properties.hibernate.temp.use_jdbc_metadata_defaults: false
  shardingsphere:
    datasource:
      names: ds-master,ds-slave,ds0-master,ds0-slave,ds1-master,ds1-slave
      ds-master:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/muses?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
        username: root
        password:
        pool-name: HikariCP-ds-master
        minimum-idle: 2
        maximum-pool-size: 10
      ds0-master:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/muses0?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
        username: root
        password:
        pool-name: HikariCP-ds0-master
        minimum-idle: 2
        maximum-pool-size: 10
      ds1-master:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/muses1?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
        username: root
        password:
        pool-name: HikariCP-ds1-master
        minimum-idle: 2
        maximum-pool-size: 10
      ds-slave:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/muses-slave?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
        username: root
        password:
        pool-name: HikariCP-ds-slave
        minimum-idle: 2
        maximum-pool-size: 10
      ds0-slave:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/muses0-slave?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
        username: root
        password:
        pool-name: HikariCP-ds0-slave
        minimum-idle: 2
        maximum-pool-size: 10
      ds1-slave:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/muses1-slave?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
        username: root
        password:
        pool-name: HikariCP-ds1-slave
        minimum-idle: 2
        maximum-pool-size: 10
    sharding:
      default-data-source-name: ds
      tables:
        tb_order:
          actual-data-nodes: ds$->{0..1}.tb_order_$->{0..1}
          database-strategy:
            inline:
              sharding-column: id
              algorithm-expression: ds$->{id % 2}
          table-strategy:
            inline:
              sharding-column: id
              algorithm-expression: tb_order_$->{id % 2}
          key-generator:
            type: SNOWFLAKE
            column: id
            props:
              worker.id: 100
      master-slave-rules:
        ds:
          master-data-source-name: ds-master
          slave-data-source-names: ds-slave
        ds0:
          master-data-source-name: ds0-master
          slave-data-source-names: ds0-slave
        ds1:
          master-data-source-name: ds1-master
          slave-data-source-names: ds1-slave
    props:
      sql.show: true
```

> 注意`ShardingJDBC`版本配置

## 单库单表

```
spring:
  shardingsphere.enabled: false
  datasource:
    url: jdbc:mysql://localhost:3306/muses?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password:
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.zaxxer.hikari.HikariDataSource
    hikari:
      pool-name: HikariCP
      auto-commit: true
      minimum-idle: 2
      maximum-pool-size: 10
  jpa:
    database: MYSQL
    show-sql: true
    open-in-view: false
    database-platform: org.hibernate.dialect.MySQL57Dialect
    hibernate:
      ddl-auto: none
      jdbc:
        batch_size: 10
        fetch_size: 10
    properties.hibernate.temp.use_jdbc_metadata_defaults: false
```

## 注意事项

本地测试不想建立太多数据库，所以都是连接单个数据库，实际只有分库没有分表。

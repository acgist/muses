# 服务Boot

提供服务注册发现，以及`service`和`dao`相关功能，没有提供`www`相关服务。

## MyBatisPlus

#### 配置

```
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
  mapper-locations:
    - classpath:/mybatis/mapper/**/*.xml
```

> 如果`mapper.xml`打包在`jar`中扫描配置`classpath*:/mybatis/mapper/**/*.xml`

#### 审计

```
@Bean
@ConditionalOnMissingBean
public MetaObjectHandler metaObjectHandler(@Autowired IdService idService) {
	return new MetaObjectHandler() {
		@Override
		public void insertFill(MetaObject metaObject) {
			final LocalDateTime now = LocalDateTime.now();
			this.setFieldValByName(BootEntity.PROPERTY_CREATE_DATE, now, metaObject);
			this.setFieldValByName(BootEntity.PROPERTY_MODIFY_DATE, now, metaObject);
		}
		@Override
		public void updateFill(MetaObject metaObject) {
			final LocalDateTime now = LocalDateTime.now();
			this.setFieldValByName(BootEntity.PROPERTY_MODIFY_DATE, now, metaObject);
		}
	};
}
```

## JPA

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
```

#### 审计

```
@EntityListeners(AuditingEntityListener.class)

@CreatedBy
@CreatedDate
@LastModifiedBy
@LastModifiedDate

@EnableJpaAuditing

@Bean
@ConditionalOnMissingBean
public AuditorAware<Long> auditorAware() {
	return () -> id;
}

@PreUpdate
@PrePersist
public void persistent() {
...
}
```

## MySQL

#### 单库单表

```
spring:
  shardingsphere.enabled: false
  datasource:
    url: jdbc:mysql://localhost:3306/muses?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&remarks=true&useInformationSchema=true
    username: root
    password:
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.zaxxer.hikari.HikariDataSource
    hikari:
      pool-name: HikariCP
      auto-commit: true
      minimum-idle: 2
      maximum-pool-size: 10
```

#### 分库分表

###### Maven

```
<dependency>
	<groupId>org.apache.shardingsphere</groupId>
	<artifactId>sharding-jdbc-spring-boot-starter</artifactId>
</dependency>
```

###### 配置

```
spring:
  shardingsphere:
    datasource:
      names: ds-master,ds-slave,ds0-master,ds0-slave,ds1-master,ds1-slave
      ds-master:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/muses?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&remarks=true&useInformationSchema=true
        username: root
        password:
        pool-name: HikariCP-ds-master
        minimum-idle: 2
        maximum-pool-size: 10
      ds0-master:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/muses0?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&remarks=true&useInformationSchema=true
        username: root
        password:
        pool-name: HikariCP-ds0-master
        minimum-idle: 2
        maximum-pool-size: 10
      ds1-master:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/muses1?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&remarks=true&useInformationSchema=true
        username: root
        password:
        pool-name: HikariCP-ds1-master
        minimum-idle: 2
        maximum-pool-size: 10
      ds-slave:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/muses-slave?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&remarks=true&useInformationSchema=true
        username: root
        password:
        pool-name: HikariCP-ds-slave
        minimum-idle: 2
        maximum-pool-size: 10
      ds0-slave:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/muses0-slave?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&remarks=true&useInformationSchema=true
        username: root
        password:
        pool-name: HikariCP-ds0-slave
        minimum-idle: 2
        maximum-pool-size: 10
      ds1-slave:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/muses1-slave?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&remarks=true&useInformationSchema=true
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

#### 注意事项

本地测试不想建立太多数据库，所以都是连接单个数据库，实际只有分表没有分库。

## ES

#### Maven

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-elasticsearch</artifactId>
	<optional>true</optional>
</dependency>
```

#### 中文分词

[https://github.com/medcl/elasticsearch-analysis-ik](https://github.com/medcl/elasticsearch-analysis-ik)

## Neo4j

#### Maven

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-neo4j</artifactId>
	<optional>true</optional>
</dependency>
```

## QueryDSL

```
<dependency>
	<groupId>com.querydsl</groupId>
	<artifactId>querydsl-jpa</artifactId>
</dependency>
<dependency>
	<groupId>com.querydsl</groupId>
	<artifactId>querydsl-apt</artifactId>
</dependency>
```

> 注意：需要安装`Eclipse`插件（`m2e-apt`）

##  版本管理

#### flyway

```
<dependency>
	<groupId>org.flywaydb</groupId>
	<artifactId>flyway-core</artifactId>
</dependency>
```

#### liquibase

```
<dependency>
	<groupId>org.liquibase</groupId>
	<artifactId>liquibase-core</artifactId>
</dependency>
```

## Excel导出

```
ExcelService
WebExcelService
ExcelServiceImpl
```

### 依赖

```
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
</dependency>
```
